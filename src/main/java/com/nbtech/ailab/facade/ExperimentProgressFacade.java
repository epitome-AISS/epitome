package com.nbtech.ailab.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.service.IExperimentPlanService;
import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.common.*;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.constant.MaterialTypeConstant;
import com.nbtech.ailab.external.facade.QuestionnaireStarFacade;
import com.nbtech.ailab.external.vo.DifyLoginVo;
import com.nbtech.ailab.util.*;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实验流程算子
 *
 * @author nber
 */
@Component
@Slf4j
public class ExperimentProgressFacade {

    @Autowired
    private ExperimentProgressDao experimentProgressDao;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ExperimentPlanFacade experimentPlanFacade;

    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private IExperimentPlanService experimentPlanService;

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private BasicModelDao basicModelDao;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private UserFacade userFacade;

    /**
     * 获取最新的实验算子
     *
     * @param dto 查询参数
     * @return
     */
    public ElementVo getProgress(ExperimentProgressDto dto)
            throws JsonProcessingException, NoSuchAlgorithmException {
        GroupsEntity groupsEntity = groupsDao.selectById(dto.getGroupsId());
        dto.setExperimentId(groupsEntity.getExperimentId());
        ElementVo elementVo = new ElementVo();
        List<ElementVo> elementVoList = groupsService.getElementVo(dto.getGroupsId());
        // 查询出当前实验进展的最新算子
        ExperimentProgressDto thisDto = experimentProgressDao.getProgress(dto.getUserId(), dto.getExperimentId(),
                dto.getGroupsId());
        // 用户id不存在 可能是无账号实验组
        if (dto.getUserId() == null) {
            // 先只处理共享链接地址
            if (LinkScopeEnum.SCOPE.name().equals(groupsEntity.getLinkScope())) {
                // 如果没有算子id 那就从头开始
                if (dto.getElementId() == null) {
                    elementVo = elementVoList.get(0);
                    getElement(elementVo, null, dto);
                    return elementVo;
                } else {
                    thisDto = new ExperimentProgressDto();
                    thisDto.setElementId(dto.getElementId());
                }
            } else {
                throw new BizException(BizResponseCodeEnum.PERSON_USERID);
            }
        }
        if (!Optional.ofNullable(thisDto).isPresent()) {
            GroupsPersonDto groupsPersonDto = groupsPersonService.getOnly(dto.getUserId(), dto.getExperimentId(),
                    dto.getGroupsId());
            groupsPersonDto.setStartTime(LocalDateTime.now());
            groupsPersonService.update(groupsPersonDto);
            // 不存在记录 证明实验是从头开始的
            elementVo = elementVoList.get(0);
            ModelSetVo modelGroupVo = getElement(elementVo, null, dto);
            dto.setElementId(elementVo.getId())
                    .setId(null)
                    .setSequence(0)
                    .setCreateDate(LocalDateTime.now())
                    .setSetId(modelGroupVo.getSetId())
                    .setModelName(modelGroupVo.getModelName());
            // 保存当前随机出来的结果
            experimentProgressDao.insert(ConvertUtils.sourceToTarget(dto, ExperimentProgressEntity.class));
            return elementVo;
        }
        // 最新算子是标识最末尾的算子 实验已经完成了
        if (ElementTypeEnum.NULLELEMENT.getDesc().equals(thisDto.getElementId())) {
            return new ElementVo().setId(ElementTypeEnum.NULLELEMENT.getDesc());
        }
        // 最新算子不是标识最末尾的算子
        Iterator<ElementVo> iterable = elementVoList.iterator();
        // 获取当前算子下标
        while (iterable.hasNext()) {
            ElementVo thisElementVo = iterable.next();
            if (thisElementVo.getId().equals(thisDto.getElementId())) {
                elementVo = thisElementVo;
                break;
            }
        }
        // 计算当前算子 返回给前端的信息
        getElement(elementVo, thisDto.getModelName(), dto);
        return elementVo;
    }

    /**
     * 完成当前算子 推进下一算子开始
     */
    public ElementVo forwardNextProgress(ExperimentProgressDto dto) throws Exception {
        dto.setMaterialId(null);
        GroupsEntity groupsEntity = groupsDao.selectById(dto.getGroupsId());
        dto.setExperimentId(groupsEntity.getExperimentId());
        // 实验者角色在实验未开始前 不可点击
        prohibition(dto);

        // 查询当前用户的最新运行记录
        if (dto.getUserId() != null) {
            ExperimentProgressDto thisDto = experimentProgressDao.getProgress(dto.getUserId(), dto.getExperimentId(),
                    dto.getGroupsId());
            if (thisDto != null) {
                dto.setElementId(thisDto.getElementId());
            }
        }
        // 获取下一算子
        ElementVo elementVo = getNextElementVo(dto);
        boolean isIntervene = ElementTypeEnum.INTERVENE.getDesc().equals(elementVo.getType());
        ObjectMapper objectMapper = new ObjectMapper();
        if (isIntervene) {
            InterventionVo interventionVo = objectMapper.convertValue(elementVo.getConfig(), InterventionVo.class);
            InterveneVo material = interventionVo.getMaterial();
            // 查询出干预的类型
            MaterialEntity entity = materialDao.selectById(material.getMaterialId());
            isIntervene = !MaterialTypeConstant.MATERIAL_GROUP.equals(entity.getMaterialType());
        }

        // 共享链接并且 下一个算子不是干预
        if (dto.getUserId() == null && !isIntervene) {
            groupsDao.addGroupPerson(1, dto.getGroupsId());
            groupsEntity.setGroupsPersonNumber(groupsEntity.getGroupsPersonNumber() + 1);
            // 保存用户并返回用户id
            SysUserEntity sysUser = saveOneUser(dto, null);
            elementVo.setUserId(sysUser.getId());
        }
        // 推算出算子应该返回前端什么信息
        ModelSetVo modelGroupVo = getElement(elementVo, null, dto);
        dto.setElementId(elementVo.getId())
                .setId(null)
                .setSequence(
                        Optional.ofNullable(elementVo.getSequence()).isPresent() ? elementVo.getSequence().intValue()
                                : null)
                .setCreateDate(LocalDateTime.now())
                .setSetId(modelGroupVo.getSetId())
                .setModelName(modelGroupVo.getModelName());
        if (dto.getUserId() != null) {
            // 推进到下一算子
            experimentProgressDao.insert(ConvertUtils.sourceToTarget(dto, ExperimentProgressEntity.class));
        }
        // 调到下一算子为空 实验完成
        if (ElementTypeEnum.NULLELEMENT.getDesc().equals(elementVo.getId())) {
            try {
                endExperiment(dto);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return elementVo;
    }


    /**
     * 创建单个用户信息 返回用户id
     *
     * @param dto
     * @param paramVo
     * @return
     * @throws NoSuchAlgorithmException
     */
    private SysUserEntity saveOneUser(ExperimentProgressDto dto, AuthenticationParamVo paramVo) throws NoSuchAlgorithmException {
        GroupsEntity groupsEntity = groupsDao.selectById(dto.getGroupsId());
        // 创建用户并且返回用户id
        String aesString = groupsDao.getKeyString(dto.getGroupsId());
        AesKeyVo aesKeyVo = JSON.parseObject(aesString, AesKeyVo.class);
        String prefixName = "EP" + dto.getExperimentId() + "-" + dto.getGroupsId();

        String userName = prefixName + CodeUtil.getStringNum(groupsEntity.getGroupsPersonNumber());
        String password = CodeUtil.fixCode(8, System.currentTimeMillis() + "PASS" + dto.getGroupsId());

        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUsername(userName);
        sysUserVo.setPassword(password);
        if (paramVo.getRealName() != null) {
            sysUserVo.setRealName(paramVo.getRealName());
        } else {
            sysUserVo.setRealName(userName);
        }
        if (paramVo.getMobile() != null) {
            sysUserVo.setMobile(paramVo.getMobile());
        }
        if (paramVo.getEmail() != null) {
            sysUserVo.setEmail(paramVo.getEmail());
        }

        SysUserEntity sysUser = userFacade.saveOneUser(sysUserVo);
        dto.setUserId(sysUser.getId());

        String secret = AesSecret.addSecret(sysUser.getOriginPassword(), aesKeyVo.getKeyArr());

        ExperimentPlanDto experiment = experimentPlanService.getPlanById(groupsEntity.getExperimentId());

        GroupsPersonEntity groupsPersonEntity = GroupsPersonEntity.builder()
                .groupsName(groupsEntity.getGroupsName())
                .groupsId(groupsEntity.getId())
                // 保存加密密码
                .password(secret)
                .experimentName(experiment.getExperimentName())
                .experimentId(groupsEntity.getExperimentId())
                .experimentStatus(CompletedStatusEnum.BEEND.getDesc())
                .experimentCode(experiment.getExperimentCode())
                .userId(sysUser.getId())
                .build();

        GroupsPersonDto groupsPersonDto = ConvertUtils.sourceToTarget(groupsPersonEntity, GroupsPersonDto.class);
        groupsPersonDto.setUserName(sysUser.getUsername());
        groupsPersonService.save(groupsPersonDto);
        return sysUser;
    }

    /**
     * 未开始的实验 实验者不可进行下一步
     */
    void prohibition(ExperimentProgressDto dto) {
        List<SysRoleEntity> sysRoleEntityList = sysRoleDao.getRole(dto.getUserId());
        ExperimentPlanDto experimentPlanDto = experimentPlanService.get(dto.getExperimentId());
        if (experimentPlanDto.getStartTime().isAfter(LocalDateTime.now()) && sysRoleEntityList.get(0).getId() == 3) {
            throw new BizException(BizResponseCodeEnum.NOT_YET_DUE);
        }
    }

    /**
     * 计算出当前算子需要返回给前端的信息
     *
     * @param modelName 为空不知道使用什么模型 不为空就是知道
     */
    ModelSetVo getElement(ElementVo elementVo, String modelName, ExperimentProgressDto dto)
            throws JsonProcessingException, NoSuchAlgorithmException {
        ModelSetVo modelGroupVo = new ModelSetVo();
        ObjectMapper objectMapper = new ObjectMapper();
        // 不存在算子
        if (!Optional.ofNullable(elementVo.getId()).isPresent()) {
            elementVo.setId(ElementTypeEnum.NULLELEMENT.getDesc());
            modelGroupVo.setModelName(modelName);
            return modelGroupVo;
        }
        // 算子类型
        ElementTypeEnum elementTypeEnum = ElementTypeEnum.fromString(elementVo.getType());
        Object object = elementVo.getConfig();
        switch (Objects.requireNonNull(elementTypeEnum)) {
            // 算子类型为模型
            case MODEL:
                ModelJsonVo modelJsonVo = objectMapper.convertValue(object, ModelJsonVo.class);
                elementVo.setMinTurns(modelJsonVo.getMinTurns());
                elementVo.setDisplayProcess(modelJsonVo.getDisplayProcess());
                ModelEntity modelEntity = modelDao.selectById(modelJsonVo.getDialogueId());
                if (!Optional.ofNullable(modelEntity).isPresent()) {
                    log.info("运行到算子为模型的时候 获取模型失败 实验组id是 {} 算子id是 {} 模型id是 {}", dto.getGroupsId(), elementVo.getId(), modelJsonVo.getDialogueId());
                    throw new BizException(BizResponseCodeEnum.EXISTS_NOT_MODEL);
                }
                if (modelEntity.getModels() != null && !modelEntity.getModels().isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<BasicModelVo> basicModelVos = mapper.readValue(modelEntity.getModels(),
                            mapper.getTypeFactory().constructCollectionType(List.class, BasicModelVo.class));
                    basicModelVos.forEach(x -> {
                        BasicModelEntity basicModelEntity = basicModelDao.selectById(x.getId());
                        x.setApiKey(basicModelEntity.getApiKey());
                        x.setName(basicModelEntity.getName());
                        x.setEnglishDesc(basicModelEntity.getEnglishDesc());
                        x.setChineseDesc(basicModelEntity.getChineseDesc());
                        x.setChineseName(basicModelEntity.getChineseName());
                        x.setEnglishName(basicModelEntity.getEnglishName());
                        x.setContextLength(basicModelEntity.getContextLength());
                        x.setType(basicModelEntity.getType());
                        x.setUrl(basicModelEntity.getUrl());
                    });
                    modelEntity.setModels(mapper.writeValueAsString(basicModelVos));
                    // 群聊算子对话 不用随机选择其中一个模型对话 而是所有模型轮流对话
                    // if (TypeModelEnum.GROUP.getValue().equals(modelEntity.getModelBotType())) {
                    // elementVo.setConfig(modelEntity);
                    // modelGroupVo.setModelName(modelEntity.getModelName());
                    // return modelGroupVo;
                    // }
                    if (modelName != null) {
                        // 上次有模型展示 那就同步展示
                        // 设置模型名称集合
                        String finalModelName = modelName;
                        BasicModelVo basicModelVo = basicModelVos.stream()
                                .filter(vo -> finalModelName.equals(vo.getName()))
                                .collect(Collectors.toList()).get(0);
                        // 设置使用模型
                        modelEntity.setModels(objectMapper.writeValueAsString(basicModelVo));
                        elementVo.setConfig(modelEntity);
                        break;
                    }
                    // 3 是多个随机 查询同实验组的数据之前用了什么模型名称 需要去掉之前使用过的模型
                    if (3 == modelEntity.getWay()) {
                        List<String> modelNames = experimentProgressDao.getModelNames(modelJsonVo.getGroupId(),
                                dto.getUserId(), dto.getGroupsId(), dto.getExperimentId());
                        if (modelNames != null) {
                            basicModelVos = basicModelVos.stream().filter(x -> !modelNames.contains(x.getName()))
                                    .collect(Collectors.toList());
                        }
                        modelGroupVo.setSetId(modelJsonVo.getGroupId());
                    }
                    // 单个随机 多个随机剩余模型 单个模型 都直接使用这个方法
                    BasicModelVo basicModelVo = getOneRand(basicModelVos);
                    modelName = basicModelVo.getName();
                    modelEntity.setModels(objectMapper.writeValueAsString(basicModelVo));
                }
                elementVo.setConfig(modelEntity);
                break;
            // 算子类型为数据收集
            case PLATE:
                break;
            case INSTRUCTION:
                break;
            case CONSENT:
                break;
            // 身份验证算子
            case AUTHENTICATION:
                break;
            // 算子类型为展示结构
            case COLLECTION:
                CollectionVo collectionVo = objectMapper.convertValue(object, CollectionVo.class);
                QuestionnaireEntity questionnaire = questionnaireDao.selectById(collectionVo.getQuestionnaireId());
                // 问卷存在基础模型 添加基础模型
                if (collectionVo.getDialogueId() != null) {
                    ModelEntity model = modelDao.selectById(collectionVo.getDialogueId());
                    List<BasicModelVo> basicModelVos = JSON.parseArray(model.getModels(), BasicModelVo.class);
                    BasicModelVo vo = basicModelVos.get(0);
                    BasicModelEntity basicModelEntity = basicModelDao.selectById(vo.getId());
                    vo.setApiKey(basicModelEntity.getApiKey());
                    vo.setName(basicModelEntity.getName());
                    vo.setEnglishDesc(basicModelEntity.getEnglishDesc());
                    vo.setChineseDesc(basicModelEntity.getChineseDesc());
                    vo.setChineseName(basicModelEntity.getChineseName());
                    vo.setEnglishName(basicModelEntity.getEnglishName());
                    vo.setContextLength(basicModelEntity.getContextLength());
                    vo.setUrl(basicModelEntity.getUrl());
                    model.setModels(JSON.toJSONString(vo));
                    questionnaire.setModelInfo(model);
                }
                elementVo.setConfig(questionnaire);
                break;
            // 算子类型为干预
            case INTERVENE:
                InterventionVo interventionVo = objectMapper.convertValue(elementVo.getConfig(), InterventionVo.class);
                InterveneVo material = interventionVo.getMaterial();
                // 查询出干预的名称
                MaterialEntity entity = materialDao.selectById(material.getMaterialId());
                if (entity == null) {
                    elementVo.setConfig(interventionVo);
                    break;
                }
                material.setName(entity.getMaterialName());
                String targetString;
                // 文本需要展示的是字符串 素材包也是从这里面取值
                if (MaterialTypeConstant.TEXT.equals(entity.getMaterialType())) {
                    // 直接把路径返回
                    material.setContent(entity.getMaterialData());
                    elementVo.setConfig(interventionVo);
                    break;
                } else if (MaterialTypeConstant.MATERIAL_GROUP.equals(entity.getMaterialType())) {
                    targetString = entity.getMaterialData();
                } else {
                    // 直接把路径返回
                    material.setContent(entity.getUrl());
                    elementVo.setConfig(interventionVo);
                    break;
                }
                List<MaterialGroupVo> list = JSON.parseArray(targetString, MaterialGroupVo.class);
                if (modelName != null) {
                    String finalModelName = modelName;
                    MaterialGroupVo materialGroupVo = list.stream().filter(x -> finalModelName.equals(x.getContent()))
                            .findFirst().get();
                    material.setContent(JSON.toJSONString(materialGroupVo));
                    elementVo.setConfig(interventionVo);
                    break;
                }
                // 素材包需要随机挑选一个结果返回
                List<String> materialNames = experimentProgressDao.getModelNames(material.getGroupId(), dto.getUserId(),
                        dto.getGroupsId(), dto.getExperimentId());
                // 返回素材id
                dto.setMaterialId(material.getMaterialId());
                // 排除出已经使用过的素材
                if (materialNames != null) {
                    list = list.stream().filter(x -> !materialNames.contains(x.getContent())).collect(Collectors.toList());
                }
                MaterialGroupVo materialGroupVo = getOneRandString(list);
                // 记录本次展示的字符串
                modelName = materialGroupVo.getContent();
                modelGroupVo.setSetId(material.getGroupId());
                material.setContent(JSON.toJSONString(materialGroupVo));
                elementVo.setConfig(interventionVo);
                break;
            case COOPERATIVE:
                CooperativeElementVo cooperativeElementVo = objectMapper.convertValue(object, CooperativeElementVo.class);
                elementVo.setConfig(cooperativeElementVo);
                break;
            case QUESTION_STAR:
                break;
            default:
                // 算子不存在
                elementVo.setId(ElementTypeEnum.NULLELEMENT.getDesc());
        }
        modelGroupVo.setModelName(modelName);
        return modelGroupVo;
    }

    /**
     * 完成身份校验算子
     *
     * @param authenParamVo 身份校验参数
     * @return
     * @throws NoSuchAlgorithmException
     */
    public Long overAuthentication(OverAuthenParamVo authenParamVo) throws NoSuchAlgorithmException {
        ExperimentProgressDto dto = new ExperimentProgressDto();
        dto.setGroupsId(authenParamVo.getGroupId());
        dto.setExperimentId(authenParamVo.getExperimentId());
        List<AuthenticationVo> authenticationVos = authenParamVo.getAuthenticationVos();
        AuthenticationParamVo paramVo = new AuthenticationParamVo();
        for (AuthenticationVo authen : authenticationVos) {
            try {
                AuthenAttributeEnum authenAttributeEnum = AuthenAttributeEnum.valueOf(authen.getAttribute());
                switch (authenAttributeEnum) {
                    case realName:
                        paramVo.setRealName(authen.getInput());
                        break;
                    case mobile:
                        paramVo.setMobile(authen.getInput());
                        break;
                    case email:
                        paramVo.setEmail(authen.getInput());
                        break;
                }
            } catch (Exception ignored) {
            }
        }
        SysUserEntity sysUser = sysUserService.authenUser(paramVo);
        if (sysUser != null) {
            // 用户存在 查询用户的id
            return sysUser.getId();
        } else {
            // 实验组总人数加一
            groupsDao.addGroupPerson(1, dto.getGroupsId());
            // 用户不存在 新增用户
            SysUserEntity newUser = saveOneUser(dto, paramVo);
            return newUser.getId();
        }
    }


    /**
     * 根据每个模型的概率随机抽取一个模型 自己不带概率 就随机抽取
     *
     * @param configList 模型集合
     * @return
     */
    public BasicModelVo getOneRand(List<BasicModelVo> configList) {
        Random random = new Random();
        return configList.get(random.nextInt(configList.size()));
    }

    /**
     * 随机抽取一个素材做展示使用
     *
     * @param materialGroupVos 素材包素材
     * @return
     */
    public MaterialGroupVo getOneRandString(List<MaterialGroupVo> materialGroupVos) {
        Random random = new Random();
        return materialGroupVos.get(random.nextInt(materialGroupVos.size()));
    }

    /**
     * 获取下一算子信息
     *
     * @return
     */
    ElementVo getNextElementVo(ExperimentProgressDto dto) {
        // 获取实验组的算子集合
        List<ElementVo> elementVoList = groupsService.getElementVo(dto.getGroupsId());
        Iterator<ElementVo> iterable = elementVoList.iterator();
        Long index = -1L;
        // 获取当前算子下标
        ElementVo thisElementVo = null;
        while (iterable.hasNext()) {
            thisElementVo = iterable.next();
            if (thisElementVo.getId().equals(dto.getElementId())) {
                index = thisElementVo.getSequence();
                break;
            }
        }
        // 没有检索到当前算子
        if (index == -1) {
            log.error("获取下一算子的时候 没有获取到算子信息 实验组id是 {} 用户id是 {} 算子的id是 {}", dto.getGroupsId(), dto.getUserId(), dto.getElementId());
            throw new BizException(BizResponseCodeEnum.EXISTS_NOT_ELEMENT);
        }
        // 当前算子是最后的一个算子
        if (index == elementVoList.size() - 1) {
            return new ElementVo();
        }
        // 获取下一算子
        return elementVoList.get((int) (index + 1L));
    }

    /**
     * 完成实验
     */
    public void endExperiment(ExperimentProgressDto dto) throws Exception {
        try {
            // 给用户标识为已完成实验
            groupsPersonDao.update(null, Wrappers.<GroupsPersonEntity>lambdaUpdate()
                    .eq(GroupsPersonEntity::getUserId, dto.getUserId())
                    .set(GroupsPersonEntity::getExperimentStatus, CompletedStatusEnum.END.getDesc())
                    .set(GroupsPersonEntity::getEndTime, LocalDateTime.now()));
            // 统计该实验计划下未完成实验的人数 人数为零 自动完成实验
            Long haveCount = groupsPersonDao.getUserExperimentId(dto.getUserId(), dto.getExperimentId(),
                    CompletedStatusEnum.BEEND.getDesc());
            if (haveCount == 0) {
                ExperimentPlanOperationRecordDto recordDto = new ExperimentPlanOperationRecordDto();
                recordDto.setOperateType(PlanStatusEnum.END.getDesc());
                recordDto.setExperimentId(groupsPersonDao.getExperimentId(dto.getUserId()));
                // 自动完成实验
                experimentPlanFacade.updatePlanStatus(recordDto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 查询当前实验组下面所有干预的信息
     */
    public List<ElementVo> getInterveneList(Long groupId) {
        List<ElementVo> elementVoList = groupsService.getElementVo(groupId);
        ObjectMapper objectMapper = new ObjectMapper();
        Long userId = ShiroUtils.getUserId();
        List<ElementVo> result = new ArrayList<>();
        for (ElementVo vo : elementVoList) {
            if (ElementTypeEnum.INTERVENE.getDesc().equals(vo.getType())) {
                InterventionVo interventionVo = objectMapper.convertValue(vo.getConfig(), InterventionVo.class);
                InterveneVo material = interventionVo.getMaterial();
                // 查询出干预的名称
                MaterialEntity entity = materialDao.selectById(material.getMaterialId());
                if (entity != null) {
                    material.setName(entity.getMaterialName());
                    if (MaterialTypeConstant.TEXT.equals(entity.getMaterialType())) {
                        material.setContent(entity.getMaterialData());
                    } else if (MaterialTypeConstant.MATERIAL_GROUP.equals(entity.getMaterialType())) {
                        // 查询当前用户这个算子的最新进度(素材包特有的)
                        ExperimentProgressDto elementProgress = experimentProgressDao.getElementProgress(userId,
                                vo.getId());
                        List<MaterialGroupVo> list = JSON.parseArray(entity.getMaterialData(), MaterialGroupVo.class);
                        if (elementProgress != null) {
                            MaterialGroupVo materialGroupVo = list.stream()
                                    .filter(x -> elementProgress.getModelName().equals(x.getContent())).findFirst()
                                    .get();
                            material.setContent(JSON.toJSONString(materialGroupVo));
                        }
                    } else {
                        material.setContent(entity.getUrl());
                    }
                    vo.setConfig(interventionVo);
                }
                result.add(vo);
            }else if (ElementTypeEnum.INSTRUCTION.getDesc().equals(vo.getType())){
                result.add(vo);
            }else if (ElementTypeEnum.CONSENT.getDesc().equals(vo.getType())){
                result.add(vo);
            }
        }
        return result;
    }

}
