package com.nbtech.ailab.facade;

import com.alibaba.fastjson.JSON;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.MaterialDao;
import com.nbtech.ailab.biz.dto.*;

import com.nbtech.ailab.biz.entity.MaterialEntity;
import com.nbtech.ailab.biz.service.IGlobalConfigurationService;
import com.nbtech.ailab.biz.service.IMaterialService;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.common.BizResponseCodeEnum;

import com.nbtech.ailab.common.RedisHeadEnum;
import com.nbtech.ailab.constant.CommonConstant;
import com.nbtech.ailab.constant.FlowStatus;
import com.nbtech.ailab.constant.MaterialTypeConstant;
import com.nbtech.ailab.util.ElementUsedUtil;
import com.nbtech.ailab.util.MinioUtil;
import com.nbtech.ailab.util.RedisService;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.MaterialGroupVo;
import com.nbtech.common.exception.BizException;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional(rollbackFor = Exception.class)
public class MaterialFacade {
    @Autowired
    private IMaterialService materialService;

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IGlobalConfigurationService globalConfigurationService;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ElementUsedUtil elementUsedUtil;

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 新建素材
     *
     * @param material material
     */
    public void saveMaterial(MaterialDto material) {
        if (material.getMaterialName() == null) {
            throw new BizException(BizResponseCodeEnum.NEW_MATERIAL_NAME_NOT_EMPTY);
        }

//        if (material.getExperimentPlanId() == null) {
//            throw new BizException(BizResponseCodeEnum.NEW_MATERIAL_EXPERIMENT_PLAN_ID_NOT_EMPTY);
//        }

        MaterialDto materialDto = materialService.getByName(material.getMaterialName());
        if (materialDto != null) {
            throw new BizException(BizResponseCodeEnum.NEW_MATERIAL_NOT_REPEAT);
        }

        // 设置归属人
        SysUserDto user = ShiroUtils.getUserEntity();
        material.setMaterialAttribution(user.getUsername());
        material.setUserId(user.getId());

        Long roleId = sysUserRoleService.getByUserId(user.getId());
        material.setRoleId(roleId);
        material.setIsDelete(0);

        // 设置当前素材状态
        material.setMaterialStatus(CommonConstant.DRAFT);
        material.setWorkFlow(FlowStatus.DISABLE);
        materialService.save(material);
    }

    /**
     * 预览minio的文件
     *
     * @param id
     * @return
     */
    public String preview(Long id) throws Exception {
        // 预览接口 需要桶加上文件夹加上文件具体名称
        return minioUtil.getPreviewUrl("questionnaire/ZS2408302113-汇总-导出款式-2024-08-30 15_03_15.xls");
    }

    /**
     * 复制素材
     *
     * @param id 素材id
     */
    public MaterialEntity copyMaterial(Long id, Long planId) {
        MaterialEntity oldMaterial = materialDao.selectById(id);
        // 设置归属人
        SysUserDto user = ShiroUtils.getUserEntity();
        oldMaterial.setMaterialAttribution(user.getUsername());
        oldMaterial.setUserId(user.getId());
        // 复制把名称加上一个 -1
        oldMaterial.setMaterialName(oldMaterial.getMaterialName() + "-1");
        oldMaterial.setCreator(null);
        oldMaterial.setCreateDate(null);
        oldMaterial.setCreateName(null);
        oldMaterial.setUpdater(null);
        oldMaterial.setUpdateName(null);
        oldMaterial.setUpdateDate(null);
        oldMaterial.setId(null);
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        oldMaterial.setRoleId(roleId);
        oldMaterial.setIsDelete(0);

        oldMaterial.setExperimentPlanId(planId);

        // 设置当前素材状态
        oldMaterial.setMaterialStatus(CommonConstant.DRAFT);
        oldMaterial.setWorkFlow(FlowStatus.DISABLE);
        materialDao.insert(oldMaterial);
        return oldMaterial;
    }

    /**
     * 素材状态流程操作
     *
     * @param dto dto
     */
    public void flow(FlowDto dto) {
        MaterialDto material = materialService.get(dto.getId());

        GlobalConfigurationDto globalConfigurationDto = globalConfigurationService.get(1L);
        Integer isEnableReview = globalConfigurationDto.getIsEnableReview();
        switch (dto.getWorkFlow()) {
            case "ENABLE":
                material.setMaterialStatus(CommonConstant.HAVE_OPEN);
                material.setWorkFlow(FlowStatus.ENABLE);
                material.setExperimentPlanId(dto.getExperimentPlanId());
                materialService.update(material);
                break;
            case "DISABLE":
                List<ParamDto> params = groupsDao.getGroupsHaveInterveneIds();
                List<Long> elementIds = params.stream().map(ParamDto::getElementId).distinct()
                        .collect(Collectors.toList());
                for (Long elementId : elementIds) {
                    if (dto.getId().equals(elementId)) {
                        throw new BizException(BizResponseCodeEnum.CURRENT_ELEMENT_HAVE_USED_NOT_DISABLE);
                    }
                }
                material.setMaterialStatus(CommonConstant.DRAFT);
                material.setWorkFlow(FlowStatus.DISABLE);
                materialService.update(material);
                break;
            case "OPEN":
                // 如果当前全局启用审核 则出现审核操作
//                if (isEnableReview == 1) {
//                    material.setMaterialStatus(CommonConstant.WAIT_REVIEW);
//                    material.setWorkFlow(FlowStatus.OPEN);
//                }

//                if (isEnableReview == 0) {
                    material.setMaterialStatus(CommonConstant.OPEN);
                    material.setWorkFlow(FlowStatus.OPEN);
//                }

                materialService.update(material);
                break;
            case "PRIVATE":
                material.setMaterialStatus(CommonConstant.HAVE_OPEN);
                material.setWorkFlow(FlowStatus.PRIVATE);
                materialService.update(material);
                materialService.deleteIsReview(dto.getId());
                break;
            default:
                break;
        }

    }

    /**
     * 操作文本 新建/修改
     *
     * @param dto dto
     */
    public void operateText(MaterialDto dto) {
        MaterialDto material = materialService.get(dto.getId());
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        if (material == null) {
            dto.setWorkFlow(FlowStatus.DISABLE);
            dto.setMaterialStatus(CommonConstant.DRAFT);
            dto.setMaterialType(MaterialTypeConstant.TEXT);
            dto.setMaterialAttribution(userName);
            dto.setUserId(user.getId());
            dto.setRoleId(roleId);
            dto.setIsDelete(0);
            materialService.save(dto);
        } else {
            // 校验是否能修改
            elementUsedUtil.validatePublishElement("intervene", dto.getId(), userName);
            materialService.update(dto);

            // //修改后同步实验组配置中引用的这个文本算子
            //
            // //构建一个实验组的集合最终用于需要修改的那些实验组
            // List<GroupsDto> finalGroups = new ArrayList<>();
            //
            // //获取未发布的素材算子id集合
            // List<Long> notPublish = elementUsedUtil.getNotPublishElementIds("intervene");
            //
            // for (Long i : notPublish) {
            // if (dto.getId().equals(i)) {
            // List<GroupsDto> groups = groupsDao.getProcessConfigStrings(i);
            // Map<Long, GroupsDto> map =
            // groups.stream().collect(Collectors.toMap(GroupsDto::getId,
            // Function.identity()));
            // Set<Long> groupIds = map.keySet();
            // for (Long groupId : groupIds) {
            // JSONArray jsonArray = JSON.parseArray(map.get(groupId).getProcessConfig());
            // for (int h = 0; h < jsonArray.size(); h++) {
            // JSONObject jsonObject = jsonArray.getJSONObject(h);
            // String type = jsonObject.getString("type");
            // if (ElementTypeEnum.INTERVENE.getDesc().equals(type)) {
            // JSONObject object =
            // jsonArray.getJSONObject(h).getJSONObject("config").getJSONObject("material");
            // if (object.getLong("materialId").equals(i)) {
            // object.put("content", dto.getMaterialData());
            // }
            // }
            // }
            // String newJsonString = jsonArray.toString();
            // GroupsDto groupsDto = new GroupsDto();
            // groupsDto.setId(groupId);
            // groupsDto.setProcessConfig(newJsonString);
            // finalGroups.add(groupsDto);
            // }
            // }
            // }
            // if (!finalGroups.isEmpty()) {
            // groupsService.updateBatchById(ConvertUtils.sourceToTarget(finalGroups,
            // GroupsEntity.class));
            // }
        }
    }

    /**
     * 审核
     *
     * @param dto dto
     */
    public void review(ReviewTestDto dto) {
        MaterialDto materialDto = materialService.get(dto.getId());
        if (dto.getIsReview() == 1) {
            materialDto.setMaterialStatus(CommonConstant.OPEN);
            materialDto.setWorkFlow(FlowStatus.OPEN);
            materialDto.setIsReview(dto.getIsReview());
        }
        if (dto.getIsReview() == 0) {
            materialDto.setMaterialStatus(CommonConstant.HAVE_OPEN);
            materialDto.setWorkFlow(FlowStatus.ENABLE);
        }
        materialService.update(materialDto);
    }

    /**
     * 上传素材
     *
     * @param file file
     */
    public FileInfoDto uploadFile(MultipartFile file) throws Exception {
        // 文件上传接口 获取文件的url路径信息
        return minioUtil.getFileUrl(file, null);
    }

    /**
     * 上传文件到问卷的文件管理文件夹
     *
     * @param file file
     */
    public String uploadQuestionnaireFile(MultipartFile file) throws Exception {
        // 文件上传接口 获取文件的url路径信息
        String fileUrl = minioUtil.getFileUrl(file, "questionnaire").getUrl();
        redisService.sSetAndTime(RedisHeadEnum.QUESTIONNAIRE.getDesc(), 3600L + 60L, fileUrl);
        redisService.sSetAndTime(RedisHeadEnum.TEMP_QUESTIONNAIRE.getDesc(), 1800L, fileUrl);
        return fileUrl;
    }

    /**
     * 定时任务 每30分钟执行一次minio文件任务
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void deleteQuestionnaireFile() {
        // 定时删除minio中的临时文件
        // redisService.del(RedisHeadEnum.QUESTIONNAIRE.getDesc());
        Set<Object> questionnaire = redisService.sGet(RedisHeadEnum.QUESTIONNAIRE.getDesc());
        Set<Object> tempQuestionnaire = redisService.sGet(RedisHeadEnum.TEMP_QUESTIONNAIRE.getDesc());
        for (Object questionnaireObj : questionnaire) {
            if (tempQuestionnaire.contains(questionnaireObj)) {
                continue;
            }
            minioUtil.removeUrl(questionnaireObj.toString());
        }
    }

    /**
     * 根据图片url删除图片
     */
    public void deleteByUrl(List<String> urls) {
        for (String url : urls) {
            minioUtil.removeUrl(url);
        }
    }

    /**
     * 删除草稿状态下未被实验组使用的素材
     *
     * @param id 素材id
     */
    public void deleteMaterial(Long id) {
        MaterialDto material = materialService.get(id);
        if (material == null) {
            return;
        }
        // 根据状态判断当前能否删除
        if (!material.getMaterialStatus().equals(CommonConstant.DRAFT)) {
            throw new BizException(BizResponseCodeEnum.NOT_DRAFT_NOT_DELETE);
        }
        materialService.deleteById(id);
        // 素材包 删除URL素材
        if (MaterialTypeConstant.MATERIAL_GROUP.equals(material.getMaterialType())) {
            List<MaterialGroupVo> materialGroupVos = JSON.parseArray(material.getMaterialData(), MaterialGroupVo.class);
            for (MaterialGroupVo materialGroupVo : materialGroupVos) {
                if ("URL".equals(materialGroupVo.getType())) {
                    minioUtil.removeUrl(materialGroupVo.getContent());
                }
            }
            // 不是文本 不是素材包 并且 url有结果
        } else if (material.getUrl() != null) {
            minioUtil.removeUrl(material.getUrl());
        }
    }
}
