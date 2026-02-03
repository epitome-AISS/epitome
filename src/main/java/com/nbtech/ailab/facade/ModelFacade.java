package com.nbtech.ailab.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.nbtech.ailab.external.facade.RedisQueueFacade;
import com.nbtech.ailab.util.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.ModelDao;
import com.nbtech.ailab.biz.dao.ModelHistoryDao;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.biz.entity.HistoryRecordEntity;
import com.nbtech.ailab.biz.entity.ModelEntity;
import com.nbtech.ailab.biz.entity.ModelHistoryEntity;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.*;
import com.nbtech.ailab.constant.CommonConstant;
import com.nbtech.ailab.constant.FlowStatus;
import com.nbtech.ailab.constant.UseModelTimeConfig;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nber
 */
@Component
@Slf4j
public class ModelFacade {

    @Autowired
    private ModelHistoryDao modelHistoryDao;

    @Autowired
    private IHistoryRecordService historyRecordService;

    @Autowired
    private IModelService modelService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private ElementUsedUtil elementUsedUtil;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private IGlobalConfigurationService globalConfigurationService;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private IBasicModelService basicModelService;

    @Autowired
    private RedisQueueFacade redisQueueFacade;

    @Autowired
    private RedisService redisService;

    /**
     * 根据实验组id,算子id（此处的算子id是指实验组配置json中的算子的id）获取算子的配置
     */
    public Object getElementConfig(ElementParamVo elementParamVo) {
        // 获取实验算子组
        List<ElementVo> elementVoList = groupsService.getElementVo(elementParamVo.getGroupId());
        Optional<ElementVo> firstMatch = elementVoList.stream()
                .filter(element -> Objects.equals(element.getId(), String.valueOf(elementParamVo.getElement())))
                .findFirst();
        if (firstMatch.isPresent()) {
            return firstMatch.get();
        } else {
            throw new BizException(BizResponseCodeEnum.EXISTS_NOT_ELEMENT);
        }
    }

    /**
     * 根据实验组id，用户id, 算子id（此处的算子id是指实验组配置json中的算子的id），获取历史问答记录
     */
    public List<HistoryRecordEntity> getRecord(ElementParamVo elementParamVo) throws Exception {
        // 获取实验算子组
        List<ElementVo> elementVoList = groupsService.getElementVo(elementParamVo.getGroupId());
        Map<String, List<ElementVo>> prodMap = elementVoList.stream()
                .collect(Collectors.groupingBy(item -> item.getType() + "_" + item.getId()));
        if (!prodMap.containsKey(ElementTypeEnum.MODEL.getDesc() + "_" + elementParamVo.getElement()) && !prodMap.containsKey(ElementTypeEnum.COLLECTION.getDesc() + "_" + elementParamVo.getElement())) {
            //
            log.info("查询历史记录的时候 未找到对应的带模型的算子 实验组id是 {} 用户id是 {} 算子id是 {}", elementParamVo.getGroupId(), elementParamVo.getUserId(), elementParamVo.getElement());
            throw new BizException(BizResponseCodeEnum.EXISTS_NOT_ELEMENT);
        }
        Object element;
        if (prodMap.containsKey(ElementTypeEnum.MODEL.getDesc() + "_" + elementParamVo.getElement())){
            element = prodMap.get(ElementTypeEnum.MODEL.getDesc() + "_" + elementParamVo.getElement()).get(0)
                    .getConfig();
        }else {
            element = prodMap.get(ElementTypeEnum.COLLECTION.getDesc() + "_" + elementParamVo.getElement()).get(0)
                    .getConfig();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ModelJsonVo modelJsonVo = objectMapper.convertValue(element, ModelJsonVo.class);
        ModelEntity modelEntity = modelDao.selectById(modelJsonVo.getDialogueId());

        List<HistoryRecordEntity> result = new ArrayList<>();

        if (modelEntity.getModels() == null || modelEntity.getModels().isEmpty()) {
            return result;
        }
        // 转json配置
        List<ModelConfigVo> configList = objectMapper.readValue(modelEntity.getModels(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ModelConfigVo.class));
        List<String> model = new ArrayList<>();
        // 配置模型名称查询参数
        configList.forEach(x -> model.add(x.getName()));
        if (CollectionUtil.isNotEmpty(model)) {
            // 获取模型对话的记录集合
            result = modelHistoryDao.getModelRecord(elementParamVo.getGroupId(), elementParamVo.getElement(),
                    elementParamVo.getUserId(), model);
        }

        boolean isOld = false;

        // 兼容性转换
        for (HistoryRecordEntity historyRecord : result) {
            List<DialogRecordDto> dialogRecords = new ArrayList<>();
            try {
                dialogRecords = JSON.parseArray(historyRecord.getRecord().toString(), DialogRecordDto.class);
            } catch (Exception e) {
                DialogRecordDto dialogRecord = new DialogRecordDto();
                dialogRecord.setType("text");
                dialogRecord.setText(historyRecord.getRecord().toString());
                dialogRecords.add(dialogRecord);
                historyRecord.setRecord(dialogRecords);
                isOld = true;
            }
        }

        if (!isOld) {
            // 图片数据处理
            for (HistoryRecordEntity historyRecord : result) {
                List<DialogRecordDto> dialogRecords = JSON.parseArray(historyRecord.getRecord().toString(),
                        DialogRecordDto.class);
                for (DialogRecordDto dialogRecord : dialogRecords) {
                    if ("image_url".equals(dialogRecord.getType())) {
                        String base64String = minioUtil.getBase64(dialogRecord.getImage_url().getUrl());
                        dialogRecord.getImage_url().setUrl(base64String);
                    }
                }
                historyRecord.setRecord(dialogRecords);
            }
        }
        return result;
    }

    /**
     * 根据实验组id，算子id（此处的算子id是指实验组配置json中的算子的id），获取最近的实验者使用的大模型
     */
    public String getLastModel(ElementParamVo elementParamVo) {
        return modelHistoryDao.getLastModel(elementParamVo);
    }

    /**
     * 根据实验组id，用户id, 算子id 模型名称（此处的算子id是指实验组配置json中的算子的id），写入新的问答记录，回答时长，问答字数
     * 需要添加user时间 那只能一次保存一问一答
     *
     * @param recordParamVo 模型对话记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveHistoryRecord(RecordParamVo recordParamVo) {
        ModelHistoryEntity modelHistory = ConvertUtils.sourceToTarget(recordParamVo, ModelHistoryEntity.class);
        List<HistoryRecordEntity> historyRecordEntities = ConvertUtils
                .sourceToTarget(recordParamVo.getHistoryRecordList(), HistoryRecordEntity.class);
        // 查询当前记录是否存在上一步对话记录 其实只用算子id和用户id对上就能判断了
        ModelHistoryEntity oldModelHistory = modelHistoryDao.selectOne(Wrappers.<ModelHistoryEntity>lambdaQuery()
                .eq(ModelHistoryEntity::getGroupsId, modelHistory.getGroupsId())
                .eq(ModelHistoryEntity::getModelName, modelHistory.getModelName())
                .eq(ModelHistoryEntity::getUserId, modelHistory.getUserId())
                .eq(ModelHistoryEntity::getElemenId, modelHistory.getElemenId())
                .orderByDesc(ModelHistoryEntity::getCreateDate)
                .last("limit 1"));
        // 上一轮对话记录存在
        if (BlankStringUtil.isBlank(oldModelHistory)) {
            for (HistoryRecordEntity user : historyRecordEntities) {
                // 只有user需要设定时间（默认记录只有一问一答）
                if (ModelRoleEnum.USER.getDesc().equals(user.getRole())) {
                    // 计算默认的时间 字数乘以默认倍率
                    BigDecimal wordNumber = Optional.ofNullable(user.getWordNumber()).isPresent()
                            ? new BigDecimal(user.getWordNumber())
                            : BigDecimal.ZERO;
                    BigDecimal defaultTime = wordNumber.multiply(UseModelTimeConfig.RATE);
                    BigDecimal maxDefaultTime = defaultTime.multiply(UseModelTimeConfig.MULTIPLE);
                    // 计算时间差
                    LocalDateTime recordTime = oldModelHistory.getCreateDate();
                    LocalDateTime nowTime = LocalDateTime.now();
                    Duration duration = Duration.between(recordTime, nowTime);
                    // 获取时间差的总秒数
                    double mill = duration.getNano() / 1_000_000_000.0;
                    BigDecimal seconds = new BigDecimal(duration.getSeconds() + mill);
                    // 如果真实时间和估算时间相差过大 就取用估算时间
                    if (seconds.compareTo(maxDefaultTime) <= 0) {
                        user.setRecordTime(seconds);
                    } else {
                        user.setRecordTime(defaultTime);
                    }
                }
            }
            // 上一轮对话不存在 直接使用默认时间
        } else {
            for (HistoryRecordEntity user : historyRecordEntities) {
                // 只有user需要设定时间（默认记录只有一问一答）
                if (ModelRoleEnum.USER.getDesc().equals(user.getRole())) {
                    // 计算默认的时间 字数乘以默认倍率
                    BigDecimal wordNumber = Optional.ofNullable(user.getWordNumber()).isPresent()
                            ? new BigDecimal(user.getWordNumber())
                            : BigDecimal.ZERO;
                    BigDecimal defaultTime = wordNumber.multiply(UseModelTimeConfig.RATE);
                    user.setRecordTime(defaultTime);
                }
            }
        }
        // 新增模型对话记录
        modelHistoryDao.insert(modelHistory);

        for (HistoryRecordEntity x : historyRecordEntities) {
            log.info("保存历史聊天记录需要转化为json的字符串 : {}", JSON.toJSONString(x.getRecord()));
            List<DialogRecordDto> array = JSON.parseArray(JSON.toJSONString(x.getRecord()), DialogRecordDto.class);
            for (DialogRecordDto record : array) {

                String type = record.getType();
                if ("image_url".equals(type)) {
                    String content = record.getImage_url().getUrl();
                    String[] parts = content.split(",");
                    String data = parts[1];
                    byte[] decodedBytes = Base64.getDecoder().decode(data);
                    String fileKey = UUID.randomUUID().toString() + ".jpg";
                    minioUtil.uploadObject(decodedBytes, fileKey);
                    record.getImage_url().setUrl(fileKey);
                }
            }

            x.setRecord(JSON.toJSONString(array));
            x.setHistoryId(modelHistory.getId());
        }

        // 批量保存模型问答历史记录
        historyRecordService.insertBatch(historyRecordEntities);
    }


    /**
     * 先把聊天记录放到redis缓存中 后续再通过定时任务写入
     *
     * @param recordParamVo
     */
    public void redisSaveRecord(RecordParamVo recordParamVo) {
        ModelHistoryEntity modelHistory = ConvertUtils.sourceToTarget(recordParamVo, ModelHistoryEntity.class);
        String head = RedisHeadEnum.USER_CHAT_COUNT.getDesc() + ":" + recordParamVo.getElemenId() + ":" + modelHistory.getUserId();
        // 保存一下当前对话轮次
        redisService.set(head, modelHistory.getTotalCount(), 60 * 60L);
        // 不直接插入 先放redis缓存里面
        redisQueueFacade.push(recordParamVo, RedisHeadEnum.SAVE_CHAT_HISTORY.getDesc());
    }

    /**
     * 获取基本模型信息
     *
     * @param groupsId  实验组id
     * @param elemenId  算子id
     * @param modelName 基本模型名称
     * @return 基本模型信息
     */
    private BasicModelDto getBasicModel(Long groupsId, String elemenId, String modelName) {
        GroupsDto groups = groupsService.get(groupsId);
        List<ProcessConfigDto> elements = JSON.parseArray(groups.getProcessConfig(), ProcessConfigDto.class);

        ProcessConfigDto processConfigDto = elements.stream()
                .filter(element -> element.getId().equals(elemenId))
                .findFirst().orElse(null);

        JSONObject jsonObject = JSON.parseObject(processConfigDto.getConfig());
        Long dialogueId = jsonObject.getLong("dialogueId");
        ModelEntity model = modelDao.selectById(dialogueId);
        List<BasicModelDto> basicModelList = JSON.parseArray(model.getModels(), BasicModelDto.class);
        BasicModelDto basicModel = basicModelList.stream()
                .filter(basicModelDto -> basicModelDto.getName().equals(modelName))
                .findFirst().orElse(null);

        return basicModelService.get(basicModel.getId());
    }

    /**
     * 获取当前模型对话的轮次
     */
    public Integer getRounds(ModelHistoryEntity modelHistory) {
        String head = RedisHeadEnum.USER_CHAT_COUNT.getDesc() + ":" + modelHistory.getElemenId() + ":" + modelHistory.getUserId();
        if (redisService.hasKey(head)) {
            Object object = redisService.get(head);
            return (Integer) object;
        }
        return Math.toIntExact(modelHistoryDao.selectCount(Wrappers.<ModelHistoryEntity>lambdaQuery()
                .eq(ModelHistoryEntity::getGroupsId, modelHistory.getGroupsId())
                .eq(ModelHistoryEntity::getUserId, modelHistory.getUserId())
                .eq(ModelHistoryEntity::getElemenId, modelHistory.getElemenId())));
    }

    /**
     * 保存模型
     *
     * @param vo vo
     */
    public void operateModel(ModelDto vo) throws Exception {
        validateModel(vo);
        // 设置归属人
        SysUserDto user = ShiroUtils.getUserEntity();
        vo.setAttribution(user.getUsername());
        vo.setUserId(user.getId());

        Long roleId = sysUserRoleService.getByUserId(user.getId());
        vo.setRoleId(roleId);
        vo.setIsDelete(0);

        // 设置模型状态和流程操作
        ModelDto modelDto = modelService.get(vo.getId());
        if (modelDto == null) {
            // 校验 experimentPlanId 必填
//            if (vo.getExperimentPlanId() == null) {
//                throw new BizException(BizResponseCodeEnum.NEW_MODEL_EXPERIMENT_PLAN_ID_NOT_EMPTY);
//            }

            ModelDto model = modelService.getByModelName(vo.getModelName());
            if (model != null) {
                throw new BizException(BizResponseCodeEnum.MODEL_NAME_NOT_REPEAT);
            }
            vo.setModelStatus(CommonConstant.DRAFT);
            vo.setWorkFlow(FlowStatus.DISABLE);
            modelService.save(vo);
        } else {
            elementUsedUtil.validatePublishElement("dialogue", vo.getId(), user.getUsername());
            modelService.update(vo);
        }
    }

    /**
     * 校验模型 对于相关填写字段的值的范围进行校验
     *
     * @param vo vo
     */
    private void validateModel(ModelDto vo) throws JsonProcessingException {
        // 1 没有模型 2 不是(智能教育的非聊天版本)
        if (vo.getModels() == null && !(Objects.equals(TypeModelEnum.EDUCATION.getValue(), vo.getModelBotType())
                && !EducationEnum.CHAT.name().equals(vo.getConfig()))) {
            throw new BizException(BizResponseCodeEnum.MODELS_NUM_NOT_EMPTY);
        }
        // 解析json格式
        if (vo.getWay() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<BasicModelDto> list = objectMapper.readValue(vo.getModels(), List.class);

            if (vo.getWay() == 1 && list.size() > 1) {
                throw new BizException(BizResponseCodeEnum.SINGLE_MODEL_NOT_MULTI);
            }

            if (vo.getWay() == 2 && list.size() < 2) {
                throw new BizException(BizResponseCodeEnum.SINGLE_MODEL_NOT_SINGLE);
            }

            if (vo.getWay() == 3 && list.size() < 2) {
                throw new BizException(BizResponseCodeEnum.MULTI_MODEL_NOT_SINGLE);
            }

            // 判断温度是否在0到1之间
            if (vo.getTemperature().compareTo(BigDecimal.ZERO) < 0
                    || vo.getTemperature().compareTo(BigDecimal.ONE) > 0) {
                throw new BizException(BizResponseCodeEnum.TEMPERATURE_BETWEEN_ZERO_AND_ONE);
            }
        }

    }

    /**
     * 模型流程操作
     *
     * @param dto dto
     */
    public void flow(FlowDto dto) {
        ModelDto model = modelService.get(dto.getId());

        GlobalConfigurationDto globalConfigurationDto = globalConfigurationService.get(1L);
        Integer isEnableReview = globalConfigurationDto.getIsEnableReview();

        switch (dto.getWorkFlow()) {
            case "DISABLE":
                List<ParamDto> params = groupsDao.getGroupsHaveDialogueIds();
                List<Long> elementIds = params.stream().map(ParamDto::getElementId).distinct()
                        .collect(Collectors.toList());
                for (Long elementId : elementIds) {
                    if (dto.getId().equals(elementId)) {
                        throw new BizException(BizResponseCodeEnum.CURRENT_ELEMENT_HAVE_USED_NOT_DISABLE);
                    }
                }
                model.setModelStatus(CommonConstant.DRAFT);
                model.setWorkFlow(FlowStatus.DISABLE);
                modelService.update(model);
                break;
            case "ENABLE":
                model.setModelStatus(CommonConstant.HAVE_OPEN);
                model.setWorkFlow(FlowStatus.ENABLE);
                modelService.update(model);
                break;
            case "OPEN":
//                if (isEnableReview == 1) {
//                    model.setModelStatus(CommonConstant.WAIT_REVIEW);
//                    model.setWorkFlow(FlowStatus.OPEN);
//                }

//                if (isEnableReview == 0) {
                    model.setModelStatus(CommonConstant.OPEN);
                    model.setWorkFlow(FlowStatus.OPEN);
//                }

                modelService.update(model);
                break;
            case "PRIVATE":
                model.setModelStatus(CommonConstant.HAVE_OPEN);
                model.setWorkFlow(FlowStatus.PRIVATE);
                modelService.update(model);
                break;
            default:
                break;
        }

    }

    /**
     * 对于模型进行审核
     *
     * @param dto dto
     */
    public void review(ReviewTestDto dto) {
        ModelDto modelDto = modelService.get(dto.getId());
        if (dto.getIsReview() == 1) {
            modelDto.setIsReview(dto.getIsReview());
            modelDto.setModelStatus(CommonConstant.OPEN);
            modelDto.setWorkFlow(FlowStatus.OPEN);
        } else {
            modelDto.setModelStatus(CommonConstant.HAVE_OPEN);
            modelDto.setWorkFlow(FlowStatus.ENABLE);
        }
        modelService.update(modelDto);
    }

    /**
     * 删除模型对话
     *
     * @param id id
     */
    public void deleteModel(Long id) {
        ModelDto model = modelService.get(id);
        if (!model.getModelStatus().equals(CommonConstant.DRAFT)) {
            throw new BizException(BizResponseCodeEnum.NOT_DRAFT_NOT_DELETE);
        }
        modelService.deleteById(id);
    }
}
