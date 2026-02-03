package com.nbtech.ailab.facade;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.*;
import com.nbtech.ailab.constant.ExperimentStyle;
import com.nbtech.common.exception.BizException;
import com.nbtech.ailab.biz.dao.ExperimentPlanDao;
import com.nbtech.ailab.biz.dao.ExperimentPlanOperationRecordDao;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.biz.service.IExperimentPlanService;
import com.nbtech.ailab.util.CodeUtil;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.OperationRecordVo;
import com.nbtech.ailab.vo.PlanStatusVo;
import com.nbtech.ailab.vo.RecordStatusVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nber
 */
@Component
public class ExperimentPlanFacade {

    @Resource
    private ExperimentPlanDao experimentPlanDao;

    @Resource
    private ExperimentPlanOperationRecordDao experimentPlanOperationRecordDao;

    @Resource
    private IExperimentPlanService experimentPlanService;

    @Resource
    private IExperimentPlanOperationRecordService experimentPlanOperationRecordService;

    @Autowired
    private IGlobalConfigurationService globalConfigurationService;

    @Resource
    private GroupsDao groupsDao;

    @Resource
    private GroupFacade groupFacade;

    @Resource
    private IExperimentMessageService experimentMessageService;

    @Resource
    private ModelDao modelDao;

    @Resource
    private QuestionnaireDao questionnaireDao;

    @Resource
    private MaterialDao materialDao;

    @Resource
    private IModelService modelService;

    @Resource
    private IQuestionnaireService questionnaireService;

    @Resource
    private MaterialFacade materialFacade;

    /**
     * 实验的添加
     *
     * @param dto 参数
     */
    public BizResponse<?> planSave(ExperimentPlanDto dto) {
        Long haveCount = experimentPlanDao.selectCount(Wrappers.<ExperimentPlanEntity>lambdaQuery()
                .eq(ExperimentPlanEntity::getExperimentName, dto.getExperimentName()));
        if (haveCount > 0) {
            return BizResponse.exception(new BizException(BizResponseCodeEnum.EXPERIMENT_NAME_REPEAT));
        }
        // 设置自动增长的编码
        dto.setExperimentCode(getExperimentCode());
        // 设置状态为私有 发布后为公开
        dto.setHoldingStatus(HoldStatusEnum.PRIVATE.getDesc());
        // 待提交状态
        dto.setExperimentStatus(PlanStatusEnum.BESUBMIT.getDesc());
        dto.setExperimentAttribution(ShiroUtils.getUserEntity().getUsername());
        if (StringUtils.isBlank(dto.getExperimentStyle())) {
            dto.setExperimentStyle(ExperimentStyle.GROUPS_PERSON);
        }

        experimentPlanService.save(dto);
        return BizResponse.success(dto.getId());
    }

    /**
     * 校验实验名称是否添加
     *
     * @param experimentName 实验名称
     */
    public boolean countName(String experimentName) {
        Long haveCount = experimentPlanDao.selectCount(Wrappers.<ExperimentPlanEntity>lambdaQuery()
                .eq(ExperimentPlanEntity::getExperimentName, experimentName));
        return haveCount <= 0;
    }

    /**
     * 复制实验
     *
     * @param id 实验id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long copyPlanAndUseElement(Long id) throws JsonProcessingException, InterruptedException {
        ExperimentPlanDto dto = experimentPlanService.getPlanById(id);
        dto.setId(null);
        dto.setCreator(null);
        dto.setCreateDate(null);
        dto.setCreateName(null);
        dto.setUpdater(null);
        dto.setUpdateDate(null);
        dto.setUpdateName(null);
        List<Long> groupIds = groupFacade.getGroupIds(id);

        // 新增实验计划
        // 设置自动增长的编码
        dto.setExperimentName(dto.getExperimentName() + "-1");
        dto.setExperimentCode(CodeUtil.getPlanCode("EP", experimentPlanService.getOldCode()));
        // 设置状态为私有 发布后为公开
        dto.setHoldingStatus(HoldStatusEnum.PRIVATE.getDesc());
        // 待提交状态
        dto.setExperimentStatus(PlanStatusEnum.BESUBMIT.getDesc());
        dto.setExperimentAttribution(ShiroUtils.getUserEntity().getUsername());
        experimentPlanService.save(dto);
        Map<String, String> copyElementMap = new HashMap<>();
        // 复制实验组
        for (Long groupId : groupIds) {
            // 复制实验组
            groupFacade.copyGroup(groupId, dto.getId(), copyElementMap);
        }
        return dto.getId();
    }

    /**
     * 复制实验
     *
     * @param id 实验id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long copyPlanAndAllElement(Long id) throws JsonProcessingException, InterruptedException {
        ExperimentPlanDto dto = experimentPlanService.getPlanById(id);
        dto.setId(null);
        dto.setCreator(null);
        dto.setCreateDate(null);
        dto.setCreateName(null);
        dto.setUpdater(null);
        dto.setUpdateDate(null);
        dto.setUpdateName(null);
        List<Long> groupIds = groupFacade.getGroupIds(id);

        // 新增实验计划
        // 设置自动增长的编码
        dto.setExperimentName(dto.getExperimentName() + "-1");
        dto.setExperimentCode(CodeUtil.getPlanCode("EP", experimentPlanService.getOldCode()));
        // 设置状态为私有 发布后为公开
        dto.setHoldingStatus(HoldStatusEnum.PRIVATE.getDesc());
        // 待提交状态
        dto.setExperimentStatus(PlanStatusEnum.BESUBMIT.getDesc());
        dto.setExperimentAttribution(ShiroUtils.getUserEntity().getUsername());
        experimentPlanService.save(dto);
        Map<String, String> copyElementMap = new HashMap<>();
        copyAllElement(dto.getId(), id, copyElementMap);
        // 复制实验组
        for (Long groupId : groupIds) {
            // 复制实验组
            groupFacade.copyGroup(groupId, dto.getId(), copyElementMap);
        }
        return dto.getId();
    }

    /**
     * 新增实验计划编号
     */
    public String getExperimentCode() {
        return CodeUtil.getPlanCode("EP", experimentPlanDao.getOldCode());
    }

    /**
     * 统计实验的 新建 待审核 待发布 进行中 已完成 个数
     */
    public PlanStatusVo getPlanStatus() {
        PlanStatusVo planStatusVo = new PlanStatusVo();
        planStatusVo.setBeAudit(PlanStatusEnum.BEAUDIT.getDesc());
        planStatusVo.setBeEnd(PlanStatusEnum.BEEND.getDesc());
        planStatusVo.setBePublish(PlanStatusEnum.BEPUBLISH.getDesc());
        planStatusVo.setBeSubmit(PlanStatusEnum.BESUBMIT.getDesc());
        planStatusVo.setEnd(PlanStatusEnum.END.getDesc());
        return experimentPlanDao.getPlanStatus(planStatusVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePlanStatus(ExperimentPlanOperationRecordDto dto) throws Exception {
        ExperimentPlanEntity experimentPlan = experimentPlanDao.selectById(dto.getExperimentId());
        GlobalConfigurationDto globalConfigurationDto = globalConfigurationService.get(1L);
        Integer isEnableReview = globalConfigurationDto.getIsEnableReview();

        // 当前实验状态
        String experimentStatus = experimentPlan.getExperimentStatus();
        String operateType = dto.getOperateType();
        String planStatus = null;
        switch (operateType) {
            case "submit":
                if (!PlanStatusEnum.BESUBMIT.getDesc().equals(experimentStatus)) {
                    throw new BizException(BizResponseCodeEnum.NOT_BESUBMIT);
                }
                // 强制验证
                if (dto.getCompulsory() == null || !dto.getCompulsory()) {
                    // 强制验证实验的实验组数量和实验组人数是否满足需求
//                    Long haveGroup = groupsDao.selectCount(Wrappers.<GroupsEntity>lambdaQuery()
//                            .eq(GroupsEntity::getExperimentId, experimentPlan.getId())
//                            .eq(GroupsEntity::getIsDeleted, 0));
//                    // 实验组数量超过
//                    if (haveGroup > experimentPlan.getGroupsNumber()) {
//                        throw new BizException(BizResponseCodeEnum.GROUP_COVER);
//                    }
//                    // 实验组数量不足
//                    if (haveGroup < experimentPlan.getGroupsNumber()) {
//                        throw new BizException(BizResponseCodeEnum.GROUP_SHORTAGE);
//                    }
                }

                if (isEnableReview == 1) {
                    planStatus = PlanStatusEnum.BEAUDIT.getDesc();
                } else {
                    planStatus = PlanStatusEnum.BEPUBLISH.getDesc();
                }

                break;
            case "audit":
                if (!PlanStatusEnum.BEAUDIT.getDesc().equals(experimentStatus)) {
                    throw new BizException(BizResponseCodeEnum.NOT_BEAUDIT);
                }
                planStatus = PlanStatusEnum.BEPUBLISH.getDesc();
                break;
            case "failsAudit":
                if (!PlanStatusEnum.BEAUDIT.getDesc().equals(experimentStatus)) {
                    throw new BizException(BizResponseCodeEnum.NOT_BEAUDIT);
                }
                planStatus = PlanStatusEnum.BESUBMIT.getDesc();
                break;
            case "publish":
                // 不是待发布且不是暂停状态
                if (!PlanStatusEnum.BEPUBLISH.getDesc().equals(experimentStatus)
                        && !PlanStatusEnum.PAUSE.getDesc().equals(experimentStatus)) {
                    throw new BizException(BizResponseCodeEnum.NOT_BEPUBLISH);
                }
                // 未通过伦理协会审核
                if (experimentPlan.getEthicsAudit() == null || !experimentPlan.getEthicsAudit()) {
                    throw new BizException(BizResponseCodeEnum.ETHICS_AUDIT);
                }
                // 实验的方案等于教案的实验发布的时候不生成人群包
                // if
                // (!ExperimentPlanProtocolEnum.TEACHING_PLAN.name().equals(experimentPlan.getProtocol()))
                // {
                List<GroupsEntity> groupsEntityList = groupsDao.selectList(Wrappers.<GroupsEntity>lambdaQuery()
                        .eq(GroupsEntity::getExperimentId, dto.getExperimentId()));
                for (GroupsEntity entity : groupsEntityList) {
                    // 调用实验组人员新增接口时 需要补充实验计划信息
                    GroupsDto groupsDto = ConvertUtils.sourceToTarget(entity, GroupsDto.class);
                    groupsDto.setExperimentName(experimentPlan.getExperimentName());
                    // 设置实验id
                    groupsDto.setExperimentId(dto.getExperimentId());
                    groupsDto.setExperimentCode(experimentPlan.getExperimentCode());
                    // 配置实验组人员信息 有聊天室算子就去创建对应的聊天室
                    groupFacade.insertGroupPerson(groupsDto);
                }
                // }

                // 发布 设置实验持有状态为开源
                experimentPlan.setHoldingStatus(HoldStatusEnum.PRIVATE.getDesc());
                experimentPlan.setPublishTime(LocalDateTime.now());
                planStatus = PlanStatusEnum.BEEND.getDesc();
                break;
            case "end":
                if (!PlanStatusEnum.BEEND.getDesc().equals(experimentStatus)) {
                    throw new BizException(BizResponseCodeEnum.NOT_BEEND);
                }
                planStatus = PlanStatusEnum.END.getDesc();
                // 完成实验要生成当天的实验图表统计结果
                List<Long> groupIds = groupsDao.getGroupIdList(experimentPlan.getId());
                LocalDate today = LocalDate.now();
                List<ExperimentMessageDto> experimentMessageDtos = new ArrayList<>();
                groupIds.forEach(x -> {
                    experimentMessageDtos.add(experimentMessageService.getExperimentMessageDto(x, today));
                });
                // 给每个实验组修订一下最终实验图表分析结果
                experimentMessageService
                        .insertBatch(ConvertUtils.sourceToTarget(experimentMessageDtos, ExperimentMessageEntity.class));
                break;
            case "pause":
                planStatus = PlanStatusEnum.PAUSE.getDesc();
                break;
            // 穿透就不修改状态
        }
        experimentPlan.setExperimentStatus(planStatus);
        // 保存记录
        experimentPlanOperationRecordService.save(dto);
        // 修改实验状态
        experimentPlanDao.updateById(experimentPlan);
    }

    /**
     * 统计实验的 新建 待审核 待发布 进行中 已完成 个数
     */
    public List<OperationRecordVo> getOperation(String id) {

        RecordStatusVo recordStatusVo = new RecordStatusVo();
        List<String> descSet = new ArrayList<>();
        // 实验计划id
        recordStatusVo.setPlanId(id);
        // 赋值查询参数 和 查询结果集
        recordStatusVo.setAudit(PlanStatusRecordEnum.AUDIT.getDesc());
        descSet.add(PlanStatusRecordEnum.AUDIT.getDesc());

        recordStatusVo.setEnd(PlanStatusRecordEnum.PUBLISH.getDesc());
        descSet.add(PlanStatusRecordEnum.PUBLISH.getDesc());

        recordStatusVo.setPublish(PlanStatusRecordEnum.END.getDesc());
        descSet.add(PlanStatusRecordEnum.END.getDesc());

        recordStatusVo.setSubmit(PlanStatusRecordEnum.PAUSE.getDesc());
        descSet.add(PlanStatusRecordEnum.PAUSE.getDesc());

        // 审核不通过 最新的审核通过或不通过记录
        recordStatusVo.setFailsAudit(PlanStatusRecordEnum.FAILSAUDIT.getDesc());
        List<OperationRecordVo> operationRecordVoList;
        operationRecordVoList = experimentPlanOperationRecordDao.getOperation(recordStatusVo);
        // 去掉查询出来的结果集合
        operationRecordVoList.forEach(x -> descSet.remove(x.getOperation()));
        // 没查出来的结果默认为空
        for (String desc : descSet) {
            OperationRecordVo opt = new OperationRecordVo();
            opt.setOperation(desc);
            operationRecordVoList.add(opt);
        }
        return operationRecordVoList;
    }

    /**
     * 更新实验计划的持有状态
     *
     * @param experimentId  实验计划id
     * @param holdingStatus 持有状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateHoldingStatus(Long experimentId, String holdingStatus) {
        ExperimentPlanEntity experimentPlan = experimentPlanDao.selectById(experimentId);
        experimentPlan.setHoldingStatus(holdingStatus);
        experimentPlanDao.updateById(experimentPlan);
    }

    /**
     * 生成元素名称，格式为：元素类型-目标id
     * 元素的名称等于 类型枚举 + '-' + 元素id
     *
     * @param elementTypeEnum 元素类型枚举
     * @param targetId        目标id
     * @return 格式化后的元素名称，如 "MODEL-123"
     */
    private String generateElementName(ElementTypeEnum elementTypeEnum, Long targetId) {
        return String.format("%S-%d", elementTypeEnum.name(), targetId);
    }

    /**
     * 复制实验计划下的所有元素
     */
    private void copyAllElement(Long newPlanId, Long oldPlanId, Map<String, String> copyElementMap)
            throws InterruptedException {
        // 遍历四个分支，每个分支查询旧实验计划下的元素并复制
        ElementTypeEnum[] elementTypes = { ElementTypeEnum.MODEL, ElementTypeEnum.COLLECTION, ElementTypeEnum.INTERVENE};

        for (ElementTypeEnum elementTypeEnum : elementTypes) {
            switch (elementTypeEnum) {
                case MODEL:
                    // 查询旧实验计划下的所有模型
                    List<ModelEntity> modelList = modelDao.selectList(
                            Wrappers.<ModelEntity>lambdaQuery()
                                    .eq(ModelEntity::getExperimentPlanId, oldPlanId));
                    // 遍历复制每个模型
                    for (ModelEntity oldModel : modelList) {
                        String modelIdName = generateElementName(elementTypeEnum, oldModel.getId());
                        if (!copyElementMap.containsKey(modelIdName)) {
                            ModelEntity newModel = modelService.copyModel(oldModel.getId(), newPlanId);
                            copyElementMap.put(modelIdName, String.valueOf(newModel.getId()));
                        }
                    }
                    break;

                case COLLECTION:
                    // 查询旧实验计划下的所有问卷
                    List<QuestionnaireEntity> questionnaireList = questionnaireDao.selectList(
                            Wrappers.<QuestionnaireEntity>lambdaQuery()
                                    .eq(QuestionnaireEntity::getExperimentPlanId, oldPlanId));
                    // 遍历复制每个问卷
                    for (QuestionnaireEntity oldQuestionnaire : questionnaireList) {
                        String collectionIdName = generateElementName(elementTypeEnum, oldQuestionnaire.getId());
                        if (!copyElementMap.containsKey(collectionIdName)) {
                            QuestionnaireEntity newQuestionnaire = questionnaireService
                                    .copyQuestionnaire(oldQuestionnaire.getId(), newPlanId);
                            copyElementMap.put(collectionIdName, String.valueOf(newQuestionnaire.getId()));
                        }
                    }
                    break;

                case INTERVENE:
                    // 查询旧实验计划下的所有素材
                    List<MaterialEntity> materialList = materialDao.selectList(
                            Wrappers.<MaterialEntity>lambdaQuery()
                                    .eq(MaterialEntity::getExperimentPlanId, oldPlanId));
                    // 遍历复制每个素材
                    for (MaterialEntity oldMaterial : materialList) {
                        String materialIdName = generateElementName(elementTypeEnum, oldMaterial.getId());
                        if (!copyElementMap.containsKey(materialIdName)) {
                            MaterialEntity newMaterial = materialFacade.copyMaterial(oldMaterial.getId(), newPlanId);
                            copyElementMap.put(materialIdName, String.valueOf(newMaterial.getId()));
                        }
                    }
                    break;
            }
        }
    }

}
