package com.nbtech.ailab.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nbtech.ailab.asyn.CountQuestionnaireRunnable;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.common.ModelRoleEnum;
import com.nbtech.ailab.common.PlanStatusEnum;
import com.nbtech.ailab.common.TypeModelEnum;
import com.nbtech.ailab.constant.MaterialTypeConstant;
import com.nbtech.ailab.util.IpUtils;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.util.UsePyUtil;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 完成数据收集工作
 *
 * @author nber
 */
@Slf4j
@Component
@EnableScheduling
public class DataCollectionFacade {

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Autowired
    private EvaluatedDimensionDao evaluatedDimensionDao;

    @Autowired
    private QuestionnaireDataDao questionnaireDataDao;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    private IQuestionnaireSelectionService questionnaireSelectionService;

    @Autowired
    private IQuestionnaireScaleService questionnaireScaleService;

    @Autowired
    private IQuestionnaireRecordService questionnaireRecordService;

    @Autowired
    private QuestionnaireSelectionDao questionnaireSelectionDao;

    @Autowired
    private QuestionnaireScaleDao questionnaireScaleDao;

    @Autowired
    private QuestionnaireRecordDao questionnaireRecordDao;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private ModelHistoryDao modelHistoryDao;

    @Autowired
    private ExperimentPlanDao experimentPlanDao;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private IExperimentMessageService experimentMessageService;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private ChatHistoryDetailDao chatHistoryDetailDao;


    @Value("${minio.useUrl}")
    String useUrl;

    /**
     * 通过ip获取当前用户的地域
     *
     * @param ip 请求ip
     */
    public void getAddress(String ip) {
        try {
            if (!ip.isEmpty()) {
                String newAddress = IpUtils.getAddress(ip);
                SysUserDto sysUserDto = ShiroUtils.getUserEntity();
                GroupsPersonEntity groupsPersonEntity = groupsPersonDao
                        .selectOne(Wrappers.<GroupsPersonEntity>lambdaUpdate()
                                .eq(GroupsPersonEntity::getUserId, sysUserDto.getId()));
                String oldAddress = groupsPersonEntity.getAddress();
                if (!newAddress.equals(oldAddress)) {
                    groupsPersonDao.update(null, Wrappers.<GroupsPersonEntity>lambdaUpdate()
                            .eq(GroupsPersonEntity::getUserId, sysUserDto.getId())
                            .set(GroupsPersonEntity::getIp, ip)
                            .set(GroupsPersonEntity::getAddress, newAddress));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 统计每个实验组下面每个模型问答的结果
     */
    List<ModelRecordEntity> getModelRecord(Long groupId) throws JsonProcessingException {
        // 获取实验组的算子集合
        List<ElementVo> elementVoList = groupsService.getElementVo(groupId);
        // 获取每个实验组大模型问答的算子
        List<ElementVo> collectionElement = elementVoList.stream()
                .filter(x -> ElementTypeEnum.MODEL.getDesc().equals(x.getType()))
                .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        List<ModelRecordEntity> result = new ArrayList<>();
        for (ElementVo element : collectionElement) {
            ModelJsonVo modelJsonVo = objectMapper.convertValue(element.getConfig(), ModelJsonVo.class);
            ModelEntity modelEntity = modelDao.selectById(modelJsonVo.getDialogueId());
            // 有的模型可以没有基础模型
            if (modelEntity.getModels() == null || modelEntity.getModels().isEmpty()) {
                continue;
            }
            // 转json配置
            List<ModelConfigVo> configList = objectMapper.readValue(modelEntity.getModels(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ModelConfigVo.class));
            List<String> names = configList.stream().map(ModelConfigVo::getName).collect(Collectors.toList());
            // 获取模型对话结果集合
            List<ModelRecordEntity> modelRecords = modelHistoryDao.getTotalRound(groupId, names,
                    ModelRoleEnum.USER.getDesc());
            for (ModelRecordEntity record : modelRecords) {
                record.setRole(ModelRoleEnum.ASSISTANT.getDesc());
                // 获取这个模型下的回答总字数和总耗时
                ModelRecordEntity modelRecord = modelHistoryDao.getTotalAnswers(record);
                record.setSpentTime(modelRecord.getSpentTime());
                record.setAnswerWords(modelRecord.getAnswerWords());
                // 获取这个模型下的总用户个数;
                modelRecord.setUserNumber(modelHistoryDao.getTotalUser(record));
                modelRecord.setRole(null);
            }
            result.addAll(modelRecords);
        }
        return result;
    }

    /**
     * 解析实验组下所有问卷结果
     *
     * @param groupId 实验组id
     */
    public void countQuestionnaire(Long groupId) {
        // 异步执行数据收集任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            // 收集问卷结果
            Runnable worker = new CountQuestionnaireRunnable(groupId,
                    questionnaireDataDao,
                    questionnaireDao,
                    questionnaireSelectionService,
                    questionnaireScaleService,
                    questionnaireRecordService,
                    questionnaireSelectionDao,
                    questionnaireScaleDao,
                    questionnaireRecordDao,
                    groupsDao);
            executorService.submit(worker);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }

    }

    /**
     * 获取实验统计数据
     *
     * @return 列表
     */
    // @Scheduled(cron = "0/60 * * * * ?")
    public void collectRecord() throws Exception {
        log.info("收集实验统计数据结果");
        // 查询所有已完成 待完成的实验
        List<String> ongoingList = new ArrayList<String>() {
            {
                add(PlanStatusEnum.BEEND.getDesc());
                add(PlanStatusEnum.END.getDesc());
            }
        };
        List<ExperimentPlanEntity> planEntityList = experimentPlanDao
                .selectList(Wrappers.<ExperimentPlanEntity>lambdaQuery()
                        .in(ExperimentPlanEntity::getExperimentStatus, ongoingList));
        for (ExperimentPlanEntity planEntity : planEntityList) {
            collectData(planEntity.getId());
        }
    }

    /**
     * 根据实验id 收集实验组数据
     *
     * @param planId 实验计划id
     * @throws JsonProcessingException
     */
    public void collectData(long planId) throws Exception {
        List<Long> groupIdList = groupsDao.getGroupIdList(planId);
        for (Long groupId : groupIdList) {
            // 收集每个实验组下实验的数据收集结果
            countQuestionnaire(groupId);
        }
    }

    /**
     * 获取实验统计数据
     *
     * @param id 实验组id
     * @return 列表
     */
    public List<ExperimentTotalVo> getExperimentTotal(Long id) {

        List<ExperimentMessageDto> endMessages = experimentMessageService.getByGroupId(id);

        /**
         * 1 实验已经完成了 那就直接输出实验结果记录表 因为在实验完成的时候 会总结统计一下完成当天的实验结果
         * 2 实验未完成 那实验图表统计表只会统计到昨天的记录 今天的没有总结出来 那只能今天的手动收集一下
         */
        String planStatus = experimentPlanDao.getSourceStatus(id);
        // 实验状态已经完成了
        if (planStatus.equals(PlanStatusEnum.END.getDesc())) {
            return ConvertUtils.sourceToTarget(endMessages, ExperimentTotalVo.class);
        } else {
            // 实验未完成 今天的数据没有统计出来 因为自动统计时间在每天凌晨统计昨天的数据
            LocalDate maxDate = LocalDate.now();
            ExperimentMessageDto experimentMessageDto = experimentMessageService.getExperimentMessageDto(id, maxDate);
            endMessages.add(experimentMessageDto);
            return ConvertUtils.sourceToTarget(endMessages, ExperimentTotalVo.class);
        }
    }

    /**
     * 获取实验计划级别的统计数据
     * 实验计划下面有多个实验组，合并所有实验组的数据
     *
     * @param planId 实验计划id
     * @return 合并后的统计数据列表
     */
    public List<ExperimentTotalVo> getExperimentTotalByPlanId(Long planId) {
        // 获取实验计划下的所有实验组ID
        List<Long> groupIdList = groupsDao.getGroupIdList(planId);

        if (groupIdList == null || groupIdList.isEmpty()) {
            return new ArrayList<>();
        }

        // 用于按日期合并数据的Map，key为日期，value为合并后的统计数据
        Map<LocalDate, ExperimentTotalVo> dateMap = new HashMap<>();

        // 遍历每个实验组，获取统计数据并合并
        for (Long groupId : groupIdList) {
            List<ExperimentTotalVo> groupTotalList = getExperimentTotal(groupId);

            // 合并每个实验组的数据
            for (ExperimentTotalVo vo : groupTotalList) {
                LocalDate recordDate = vo.getRecordDate();
                if (recordDate == null) {
                    continue;
                }

                // 如果该日期已存在，则累加数据
                if (dateMap.containsKey(recordDate)) {
                    ExperimentTotalVo existingVo = dateMap.get(recordDate);
                    existingVo.setProcessingNumber(
                            (existingVo.getProcessingNumber() == null ? 0L : existingVo.getProcessingNumber()) +
                                    (vo.getProcessingNumber() == null ? 0L : vo.getProcessingNumber()));
                    existingVo.setFinishedNumber(
                            (existingVo.getFinishedNumber() == null ? 0L : existingVo.getFinishedNumber()) +
                                    (vo.getFinishedNumber() == null ? 0L : vo.getFinishedNumber()));
                    existingVo.setSumNumber(
                            (existingVo.getSumNumber() == null ? 0L : existingVo.getSumNumber()) +
                                    (vo.getSumNumber() == null ? 0L : vo.getSumNumber()));
                } else {
                    // 如果该日期不存在，则直接添加
                    ExperimentTotalVo newVo = new ExperimentTotalVo();
                    newVo.setRecordDate(recordDate);
                    newVo.setProcessingNumber(vo.getProcessingNumber() == null ? 0L : vo.getProcessingNumber());
                    newVo.setFinishedNumber(vo.getFinishedNumber() == null ? 0L : vo.getFinishedNumber());
                    newVo.setSumNumber(vo.getSumNumber() == null ? 0L : vo.getSumNumber());
                    dateMap.put(recordDate, newVo);
                }
            }
        }

        // 将Map转换为List，并按日期排序
        List<ExperimentTotalVo> result = new ArrayList<>(dateMap.values());
        result.sort(Comparator.comparing(ExperimentTotalVo::getRecordDate));

        return result;
    }

    /**
     * 获取地域分布统计数据
     *
     * @param id
     * @return
     */
    public List<AddressTotalDto> getAddressTotal(Long id) {
        List<AddressTotalDto> list = groupsPersonService.getAddressByGroupId(id);
        List<AddressTotalDto> addresses = new ArrayList<>();
        if (!list.isEmpty()) {
            Long totalCount = list.stream().mapToLong(AddressTotalDto::getNumber).sum();
            for (AddressTotalDto address : list) {
                Double perCent = Math.round((address.getNumber() / (double) totalCount) * 100.0) / 100.0;
                address.setPerCent(perCent);
                addresses.add(address);
            }
        }
        return list;
    }

    /**
     * 获取实验计划级别的地域分布统计数据
     * 实验计划下面有多个实验组，合并所有实验组的地域数据
     *
     * @param planId 实验计划id
     * @return 合并后的地域统计数据列表（只包含数量，不包含百分比）
     */
    public List<AddressTotalDto> getAddressTotalByPlanId(Long planId) {
        // 获取实验计划下的所有实验组ID
        List<Long> groupIdList = groupsDao.getGroupIdList(planId);

        if (groupIdList == null || groupIdList.isEmpty()) {
            return new ArrayList<>();
        }

        // 用于按地域名称合并数据的Map，key为地域名称，value为合并后的统计数据
        Map<String, AddressTotalDto> addressMap = new HashMap<>();

        // 遍历每个实验组，获取地域统计数据并合并
        for (Long groupId : groupIdList) {
            List<AddressTotalDto> groupAddressList = groupsPersonService.getAddressByGroupId(groupId);

            // 合并每个实验组的地域数据
            for (AddressTotalDto dto : groupAddressList) {
                String addressName = dto.getName();
                if (addressName == null) {
                    continue;
                }

                // 如果该地域已存在，则累加数量
                if (addressMap.containsKey(addressName)) {
                    AddressTotalDto existingDto = addressMap.get(addressName);
                    Long existingNumber = existingDto.getNumber() == null ? 0L : existingDto.getNumber();
                    Long newNumber = dto.getNumber() == null ? 0L : dto.getNumber();
                    existingDto.setNumber(existingNumber + newNumber);
                } else {
                    // 如果该地域不存在，则直接添加
                    AddressTotalDto newDto = new AddressTotalDto();
                    newDto.setName(addressName);
                    newDto.setNumber(dto.getNumber() == null ? 0L : dto.getNumber());
                    // 不设置百分比
                    addressMap.put(addressName, newDto);
                }
            }
        }

        // 将Map转换为List
        return new ArrayList<>(addressMap.values());
    }

    /**
     * 获取实验计划级别的完成人数统计
     * 统计实验计划下所有实验组的用户完成情况
     *
     * @param planId 实验计划id
     * @return 完成情况统计
     */
    public PlanCompletionVo getPlanCompletion(Long planId) {
        PlanCompletionVo result = new PlanCompletionVo();

        // 1. 统计实验计划级别的完成人数和总人数
        // 完成人数：experiment_id = planId 且 end_time is not null
        Long planFinishedNumber = groupsPersonDao.selectCount(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getExperimentId, planId)
                        .isNotNull(GroupsPersonEntity::getEndTime));

        // 总人数：experiment_id = planId
        Long planTotalNumber = groupsPersonDao.selectCount(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getExperimentId, planId));

        // 计算实验计划级别的完成百分比（保留两位小数）
        BigDecimal planCompletionPercent = BigDecimal.ZERO;
        if (planTotalNumber != null && planTotalNumber > 0) {
            planCompletionPercent = BigDecimal.valueOf(planFinishedNumber)
                    .divide(BigDecimal.valueOf(planTotalNumber), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        result.setPlanFinishedNumber(planFinishedNumber == null ? 0L : planFinishedNumber);
        result.setPlanTotalNumber(planTotalNumber == null ? 0L : planTotalNumber);
        result.setPlanCompletionPercent(planCompletionPercent);

        // 2. 统计每个实验组的完成情况
        // 获取实验计划下的所有实验组ID
        List<Long> groupIdList = groupsDao.getGroupIdList(planId);
        List<PlanCompletionVo.GroupCompletionVo> groupCompletionList = new ArrayList<>();

        if (groupIdList != null && !groupIdList.isEmpty()) {
            // 获取所有实验组信息（用于获取实验组名称）
            Map<Long, GroupsEntity> groupMap = new HashMap<>();
            for (Long groupId : groupIdList) {
                GroupsEntity group = groupsDao.selectById(groupId);
                if (group != null) {
                    groupMap.put(groupId, group);
                }
            }

            // 遍历每个实验组，统计完成情况
            for (Long groupId : groupIdList) {
                PlanCompletionVo.GroupCompletionVo groupVo = new PlanCompletionVo.GroupCompletionVo();
                groupVo.setGroupId(groupId);

                // 获取实验组名称
                GroupsEntity group = groupMap.get(groupId);
                if (group != null) {
                    groupVo.setGroupName(group.getGroupsName());
                }

                // 统计该实验组的完成人数：groups_id = groupId 且 end_time is not null
                Long groupFinishedNumber = groupsPersonDao.selectCount(
                        Wrappers.<GroupsPersonEntity>lambdaQuery()
                                .eq(GroupsPersonEntity::getGroupsId, groupId)
                                .isNotNull(GroupsPersonEntity::getEndTime));

                // 统计该实验组的总人数：groups_id = groupId
                Long groupTotalNumber = groupsPersonDao.selectCount(
                        Wrappers.<GroupsPersonEntity>lambdaQuery()
                                .eq(GroupsPersonEntity::getGroupsId, groupId));

                // 计算实验组级别的完成百分比（保留两位小数）
                BigDecimal groupCompletionPercent = BigDecimal.ZERO;
                if (groupTotalNumber != null && groupTotalNumber > 0) {
                    groupCompletionPercent = BigDecimal.valueOf(groupFinishedNumber)
                            .divide(BigDecimal.valueOf(groupTotalNumber), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                groupVo.setGroupFinishedNumber(groupFinishedNumber == null ? 0L : groupFinishedNumber);
                groupVo.setGroupTotalNumber(groupTotalNumber == null ? 0L : groupTotalNumber);
                groupVo.setGroupCompletionPercent(groupCompletionPercent);

                groupCompletionList.add(groupVo);
            }
        }

        result.setGroupCompletionList(groupCompletionList);

        return result;
    }

    /**
     * 解析问卷的json 获取选项顺序和内容
     */
    private List<ChoiceDto> jsonToObject(Long questionnaireId, Long questionSort) {
        QuestionInfoDto question = questionnaireDataDao.getByQuestionSort(questionnaireId, questionSort);
        JSONArray jsonArray = JSON.parseArray(question.getChoices());

        List<ChoiceDto> choices = jsonArray.toJavaList(ChoiceDto.class);

        return choices;
    }

    /**
     * 获取问卷数据分析
     *
     * @param dto dto
     * @return 问卷数据集合
     */
    public List<QuestionnaireBoardVo> getQuestionnaireDataTotal(QuestionnaireDataParamDto dto) throws Exception {
        // 构造一个问卷数据分析的集合
        List<QuestionnaireBoardVo> newList = new ArrayList<>();

        // 创建文字类型的问卷数据分析
        List<QuestionnaireBoardVo> wordList = createWordAnswer(dto);
        newList.addAll(wordList);

        // 创建排序题的问卷数据分析
        List<QuestionnaireBoardVo> sortList = createSortAnswer(dto);
        newList.addAll(sortList);

        // 创建选项类型的问卷数据分析
        List<QuestionnaireBoardVo> singleList = createOptionAnswer(dto.getQuestionnaireId(), dto.getGroupsId());
        newList.addAll(singleList);

        // 创建多选类型的问卷数据分析
        List<QuestionnaireBoardVo> multiList = createMultiAnswer(dto.getQuestionnaireId(), dto.getGroupsId());
        newList.addAll(multiList);

        // 创建量表类型的问卷数据分析
        List<QuestionnaireBoardVo> scaleList = createScaleAnswer(dto.getQuestionnaireId(), dto.getGroupsId());
        newList.addAll(scaleList);

        return newList;
    }

    // 创建文字类型的问卷数据分析
    private List<QuestionnaireBoardVo> createWordAnswer(QuestionnaireDataParamDto dto) throws Exception {
        List<QuestionnaireBoardVo> wordVoList = new ArrayList<>();
        // 获取当前问卷下所有的填空和问答题
        List<QuestionDto> wordQuestions = questionnaireDataDao.getWords(dto.getQuestionnaireId());
        if (!wordQuestions.isEmpty()) {
            for (QuestionDto q : wordQuestions) {
                QuestionnaireBoardVo vo = new QuestionnaireBoardVo();
                // 设置题目名称题目类型 题目顺序
                vo.setQuestionName(q.getQuestionName());
                vo.setQuestionType(q.getQuestionType());
                vo.setSort(q.getQuestionSort());
                Integer textNum = questionnaireDao.getTextUsefully(dto.getGroupsId(), dto.getQuestionnaireId(),
                        q.getQuestionSort());
                String questionSort = q.getQuestionSort().toString();
                vo.setTotalNum(textNum);
                PyParamVo pyParamVo = new PyParamVo();
                // 获取图片需要拿到minio的路径
                pyParamVo.setMinioPath(useUrl);
                pyParamVo.setExperimentId(dto.getExperimentId());
                pyParamVo.setQuestionnaireId(dto.getQuestionnaireId());
                pyParamVo.setGroupId(dto.getGroupsId());
                pyParamVo.setQuestionSort(questionSort);
                String url = UsePyUtil.getUrl(pyParamVo);
                vo.setUrl(url);
                wordVoList.add(vo);

            }
        }

        return wordVoList;
    }

    // 创建排序类型的问卷数据分析
    private List<QuestionnaireBoardVo> createSortAnswer(QuestionnaireDataParamDto dto) throws Exception {
        List<QuestionnaireBoardVo> wordVoList = new ArrayList<>();
        // 获取当前问卷下所有 排序题目
        List<QuestionDto> wordQuestions = questionnaireDataDao.getSort(dto.getQuestionnaireId());
        if (!wordQuestions.isEmpty()) {
            for (QuestionDto q : wordQuestions) {
                QuestionnaireBoardVo vo = new QuestionnaireBoardVo();
                // 设置题目名称题目类型 题目顺序
                vo.setQuestionName(q.getQuestionName());
                vo.setQuestionType(q.getQuestionType());
                vo.setSort(q.getQuestionSort());
                Integer textNum = questionnaireDao.getTextUsefully(dto.getGroupsId(), dto.getQuestionnaireId(),
                        q.getQuestionSort());
                List<WordTypeDto> contexts = questionnaireDao.getAnswerList(dto.getGroupsId(), dto.getQuestionnaireId(),
                        q.getQuestionSort());
                vo.setTotalNum(textNum);
                vo.setContexts(contexts);
                wordVoList.add(vo);
            }
        }
        return wordVoList;
    }

    /**
     * 创建单选类型的问卷数据分析
     */
    private List<QuestionnaireBoardVo> createOptionAnswer(Long questionnaireId, Long groupsId) {
        List<QuestionnaireBoardVo> optionVoList = new ArrayList<>();
        // 获取当前问卷下所有单选题
        List<QuestionDto> singleQuestions = questionnaireDataDao.getSingles(questionnaireId);
        // 单选题存在时 对每一道单选题进行操作
        if (!singleQuestions.isEmpty()) {
            for (QuestionDto q : singleQuestions) {
                QuestionnaireBoardVo vo = new QuestionnaireBoardVo();

                // 保存问题题目 问题类型 问题顺序
                vo.setQuestionName(q.getQuestionName());
                vo.setQuestionType(q.getQuestionType());
                vo.setSort(q.getQuestionSort());
                // 收集单选类型题目的答案
                List<WordQuestionnaireDto> wordQuestionnaires = questionnaireDao.getSingleChoicesAnswers(groupsId,
                        questionnaireId, q.getQuestionSort());
                List<WordQuestionnaireDto> finalList = new ArrayList<>();
                List<WordQuestionnaireDto> distinctList = wordQuestionnaires.stream().distinct()
                        .collect(Collectors.toList());

                // 对于当前已经很选择的选项进行处理
                for (int i = 0; i < distinctList.size(); i++) {
                    int finalI = i;
                    List<WordQuestionnaireDto> commonList = wordQuestionnaires.stream()
                            .filter(x -> x.equals(distinctList.get(finalI))).collect(Collectors.toList());
                    WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                    wordQuestionnaireDto.setOptionSort(distinctList.get(finalI).getOptionSort());
                    wordQuestionnaireDto.setOptionName(distinctList.get(finalI).getOptionName());
                    wordQuestionnaireDto.setCountNum(commonList.stream().count());
                    finalList.add(wordQuestionnaireDto);
                }

                // 对于未被选择的选项进行处理
                List<ChoiceDto> choices = jsonToObject(questionnaireId, q.getQuestionSort());
                List<Long> sorts = distinctList.stream().map(WordQuestionnaireDto::getOptionSort)
                        .collect(Collectors.toList());
                if (choices.size() != distinctList.size()) {
                    for (ChoiceDto c : choices) {
                        if (!sorts.contains(c.getChoiceSort())) {
                            WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                            wordQuestionnaireDto.setCountNum(0L);
                            wordQuestionnaireDto.setOptionSort(c.getChoiceSort());
                            wordQuestionnaireDto.setOptionName(c.getChoiceContext());
                            finalList.add(wordQuestionnaireDto);
                        }

                    }
                }

                vo.setWordQuestionnaires(finalList);
                vo.setTotalNum(wordQuestionnaires.size());
                optionVoList.add(vo);

            }

        }

        return optionVoList;
    }

    /**
     * 创建多选类型的问卷数据分析
     */
    private List<QuestionnaireBoardVo> createMultiAnswer(Long questionnaireId, Long groupsId) {
        List<QuestionnaireBoardVo> multiVoList = new ArrayList<>();

        // 获取当前问卷下所有的多选题
        List<QuestionDto> multiQuestions = questionnaireDataDao.getMultis(questionnaireId);
        if (!multiQuestions.isEmpty()) {
            for (QuestionDto q : multiQuestions) {
                QuestionnaireBoardVo vo = new QuestionnaireBoardVo();
                vo.setQuestionName(q.getQuestionName());
                vo.setQuestionType(q.getQuestionType());
                vo.setSort(q.getQuestionSort());

                List<WordQuestionnaireDto> wordQuestionnaires = new ArrayList<>();
                List<WordQuestionnaireDto> finalList = new ArrayList<>();
                List<String> jsonString = questionnaireDao.getMultiChoicesAnswers(groupsId, questionnaireId,
                        q.getQuestionSort());
                // 获取到所有的多选答案 进行统计处理
                for (String s : jsonString) {
                    JSONArray jsonArray = JSON.parseArray(s);
                    jsonArray.remove(null);
                    if (jsonArray.isEmpty()) {
                        continue;
                    }
                    for (int i = 0; i < jsonArray.size(); i++) {
                        WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Long choiceSort = jsonObject.getLong("choiceSort");
                        String choiceContext = jsonObject.getString("choiceContext");
                        wordQuestionnaireDto.setOptionSort(choiceSort);
                        wordQuestionnaireDto.setOptionName(choiceContext);
                        wordQuestionnaires.add(wordQuestionnaireDto);
                    }
                }

                List<WordQuestionnaireDto> distinctList = wordQuestionnaires.stream().distinct()
                        .collect(Collectors.toList());

                // 处理已经有的答案
                for (int i = 0; i < distinctList.size(); i++) {
                    int finalI = i;
                    List<WordQuestionnaireDto> commonList = wordQuestionnaires.stream()
                            .filter(x -> x.equals(distinctList.get(finalI))).collect(Collectors.toList());
                    WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                    wordQuestionnaireDto.setOptionName(distinctList.get(finalI).getOptionName());
                    wordQuestionnaireDto.setOptionSort(distinctList.get(finalI).getOptionSort());
                    wordQuestionnaireDto.setCountNum(commonList.stream().count());
                    finalList.add(wordQuestionnaireDto);
                }

                // 对于未被选择的选项进行处理
                List<ChoiceDto> choices = jsonToObject(questionnaireId, q.getQuestionSort());
                List<Long> sorts = distinctList.stream().map(WordQuestionnaireDto::getOptionSort)
                        .collect(Collectors.toList());
                if (choices.size() != distinctList.size()) {
                    for (ChoiceDto c : choices) {
                        if (!sorts.contains(c.getChoiceSort())) {
                            WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                            wordQuestionnaireDto.setCountNum(0L);
                            wordQuestionnaireDto.setOptionSort(c.getChoiceSort());
                            wordQuestionnaireDto.setOptionName(c.getChoiceContext());
                            finalList.add(wordQuestionnaireDto);
                        }

                    }
                }
                int totalNum = 0;
                for (WordQuestionnaireDto w : finalList) {
                    totalNum += w.getCountNum();

                }

                vo.setWordQuestionnaires(finalList);
                vo.setEffectiveNum(jsonString.size());
                vo.setTotalNum(totalNum);

                multiVoList.add(vo);

            }
        }

        return multiVoList;
    }

    private List<QuestionnaireBoardVo> createScaleAnswer(Long questionnaireId, Long groupsId) {
        List<QuestionnaireBoardVo> scaleVoList = new ArrayList<>();

        List<AnswerJsonDto> answerDtoList = questionnaireDataDao.getScaleAnswer(groupsId, questionnaireId);
        List<AnswerJsonDto> newAnswerList = new ArrayList<>();

        // 获取量表类型的问题
        for (AnswerJsonDto a : answerDtoList) {
            QuestionDto question = questionnaireDataDao.getScaleQuestion(questionnaireId, a.getAnswerSort());
            if (question != null) {
                newAnswerList.add(a);
            }

        }
        // 聚合量表答案
        Map<Long, List<AnswerJsonDto>> scaleAnswersMap = new HashMap<>();
        for (AnswerJsonDto answerDto : newAnswerList) {
            Long answerSort = answerDto.getAnswerSort(); // 获取answerSort作为键
            List<AnswerJsonDto> answers = scaleAnswersMap.getOrDefault(answerSort, new ArrayList<>());
            answers.add(answerDto); // 将当前AnswerDto对象添加到对应answerSort的列表中
            scaleAnswersMap.put(answerSort, answers); // 更新Map
        }

        for (Map.Entry<Long, List<AnswerJsonDto>> entry : scaleAnswersMap.entrySet()) {
            int totalNum = 0;
            Long answerSort = entry.getKey(); // 获取键，即answerSort
            List<WordQuestionnaireDto> wordQuestionnaires = new ArrayList<>();
            QuestionnaireBoardVo optionVo = new QuestionnaireBoardVo();

            List<AnswerJsonDto> list = questionnaireDataDao.getScaleContexts(groupsId, questionnaireId, answerSort);
            List<String> optionList = list.stream().map(AnswerJsonDto::getScaleContext).distinct()
                    .collect(Collectors.toList());
            for (int i = 0; i < optionList.size(); i++) {
                int finalI = i;
                List<AnswerJsonDto> operateList = list.stream()
                        .filter(x -> x.getScaleContext().equals(optionList.get(finalI))).collect(Collectors.toList());
                List<Long> scaleGrades = operateList.stream().map(AnswerJsonDto::getScaleGrade)
                        .collect(Collectors.toList());
                WordQuestionnaireDto wordQuestionnaire = new WordQuestionnaireDto();
                wordQuestionnaire.setOptionName(optionList.get(finalI));
                wordQuestionnaire.setCountNum(operateList.stream().count());
                wordQuestionnaire.setOptionSort(scaleGrades.get(0));
                wordQuestionnaires.add(wordQuestionnaire);
                totalNum += operateList.stream().count();
            }

            QuestionInfoDto questionInfo = questionnaireDataDao.getQuestionInfo(questionnaireId, answerSort);
            JSONArray jsonArray = JSON.parseArray(questionInfo.getScales());
            // 构建一个用于存放问题所有选项的集合
            List<String> jsonList = new ArrayList<>();
            Map<String, Long> jsonMap = new HashMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long scaleGrade = jsonObject.getLong("scaleGrade");
                String scaleContext = jsonObject.getString("scaleContext");
                jsonList.add(scaleContext);
                jsonMap.put(scaleContext, scaleGrade);
            }

            List<String> newList = new ArrayList<>();
            for (String s : jsonList) {
                if (!optionList.contains(s)) {
                    newList.add(s);
                }
            }

            if (!newList.isEmpty()) {
                for (String s : newList) {
                    WordQuestionnaireDto wordQuestionnaireDto = new WordQuestionnaireDto();
                    wordQuestionnaireDto.setOptionName(s);
                    wordQuestionnaireDto.setCountNum(0L);
                    wordQuestionnaireDto.setOptionSort(jsonMap.get(s));
                    wordQuestionnaires.add(wordQuestionnaireDto);
                }
            }

            optionVo.setSort(answerSort);
            QuestionDto question = questionnaireDataDao.getBySort(questionnaireId, answerSort);
            optionVo.setQuestionName(question.getQuestionName());
            optionVo.setQuestionType(question.getQuestionType());
            optionVo.setWordQuestionnaires(wordQuestionnaires);
            optionVo.setTotalNum(totalNum);
            scaleVoList.add(optionVo);

        }
        return scaleVoList;
    }

    /**
     * 智能教育首页数据统计
     *
     * 教师数 = 实验者数量
     * 学生数 = 被试者数量
     * 公开课堂数 = 除了被删除的所有的实验数量
     * 开源教具数 = 开源的：素材+问卷+聊天室+对话机器人数量
     */
    public IEducation getIEducation() {
        // 统计教师数量
        Integer teacherNumber = sysUserService.getTeacherNumber();
        // 统计所有的学生数量
        Integer studentNumber = experimentPlanDao.getStudentNumber();
        // 统计所有公开课堂数量
        Integer publicClassNumber = experimentPlanDao.getPublicClassNumber();
        // 统计所有的开源教具数量
        // 1 开源素材
        List<String> materialTypeList = new ArrayList<>();
        Field[] fields = MaterialTypeConstant.class.getFields();
        for (Field field : fields) {
            try {
                // 获取字段的值
                String value = (String) field.get(null);
                materialTypeList.add(value);
            } catch (IllegalAccessException ignored) {
            }
        }
        Integer publicMaterialNumber = materialService.getPublicMaterialNumber(materialTypeList);
        // 2 开源问卷
        Integer questionnaireNumber = questionnaireDao.getQuestionnaireNumber();
        // 4 对话机器人
        List<Integer> modelBotTypeList = new ArrayList<>();
        modelBotTypeList.add(TypeModelEnum.SINGLE.getValue());
        modelBotTypeList.add(TypeModelEnum.EDUCATION.getValue());
        Integer modelCount = modelDao.getModelCount(modelBotTypeList);
        // 统计数量和
        Integer publicAidsNumber = publicMaterialNumber + questionnaireNumber +  modelCount;
        // 返回所有的结果
        return IEducation.builder().teacherNumber(teacherNumber)
                .studentNumber(studentNumber)
                .publicClassNumber(publicClassNumber)
                .OpenSourceAids(publicAidsNumber).build();
    }

    /**
     * 获取实验计划下实验组的问卷统计数据
     * 1. 计算题目类型在总题型的占用的百分比（保留到小数点后两位）
     * 2. 计算有效答题数量
     * 3. 计算平均答题时间（分钟）
     *
     * @param dto 查询参数（包含实验计划id、实验组id和问卷id）
     * @return 问卷统计数据列表（每个问卷一条记录）
     */
    public List<QuestionnaireStatisticsVo> getQuestionnaireStatistics(QuestionnaireDataParamDto dto) {
        List<QuestionnaireStatisticsVo> resultList = new ArrayList<>();

        if (dto == null || dto.getExperimentId() == null || dto.getGroupsId() == null) {
            return resultList;
        }

        Long experimentPlanId = dto.getExperimentId();
        Long groupId = dto.getGroupsId();

        // 验证实验组是否属于该实验计划
        GroupsEntity group = groupsDao.selectById(groupId);
        if (group == null || !group.getExperimentId().equals(experimentPlanId)) {
            return resultList;
        }

        // 获取该实验组下的问卷ID列表
        Set<Long> questionnaireIdSet = new HashSet<>();
        if (dto.getQuestionnaireId() != null) {
            // 如果指定了问卷id，只统计该问卷
            questionnaireIdSet.add(dto.getQuestionnaireId());
        } else {
            // 如果没有指定问卷id，统计该实验组下的所有问卷
            List<QuestionnaireDto> questionnaireList = questionnaireDataDao.getQuestionnaireIdsByGroupId(groupId);
            if (questionnaireList != null) {
                for (QuestionnaireDto questionnaire : questionnaireList) {
                    if (questionnaire.getId() != null) {
                        questionnaireIdSet.add(questionnaire.getId());
                    }
                }
            }
        }

        // 遍历每个问卷，分别统计
        for (Long questionnaireId : questionnaireIdSet) {
            QuestionnaireStatisticsVo result = calculateQuestionnaireStatistics(groupId, questionnaireId);
            resultList.add(result);
        }

        return resultList;
    }

    /**
     * 计算单个问卷的统计数据
     *
     * @param groupId         实验组id
     * @param questionnaireId 问卷id
     * @return 问卷统计数据
     */
    private QuestionnaireStatisticsVo calculateQuestionnaireStatistics(Long groupId, Long questionnaireId) {
        QuestionnaireStatisticsVo result = new QuestionnaireStatisticsVo();
        result.setQuestionnaireId(questionnaireId);

        // 获取问卷信息
        QuestionnaireEntity questionnaireEntity = questionnaireDao.selectById(questionnaireId);
        if (questionnaireEntity != null) {
            result.setQuestionnaireName(questionnaireEntity.getQuestionnaireName());
        }

        // 1. 统计题目类型占比
        Map<String, Integer> questionTypeCountMap = new HashMap<>();
        int totalQuestionCount = 0;

        if (questionnaireEntity != null && questionnaireEntity.getQuestionnaireData() != null) {
            try {
                // 解析问卷数据JSON
                JSONArray questionArray = JSON.parseArray(questionnaireEntity.getQuestionnaireData());
                for (int i = 0; i < questionArray.size(); i++) {
                    JSONObject questionObj = questionArray.getJSONObject(i);
                    String questionType = questionObj.getString("questionType");
                    if (questionType != null && !questionType.isEmpty()) {
                        questionTypeCountMap.put(questionType, questionTypeCountMap.getOrDefault(questionType, 0) + 1);
                        totalQuestionCount++;
                    }
                }
            } catch (Exception e) {
                log.error("解析问卷数据失败，questionnaireId: {}", questionnaireId, e);
            }
        }

        // 计算题目类型占比（保留两位小数），并将英文类型转换为中文描述
        Map<String, BigDecimal> questionTypePercentages = new HashMap<>();
        if (totalQuestionCount > 0) {
            for (Map.Entry<String, Integer> entry : questionTypeCountMap.entrySet()) {
                BigDecimal percentage = BigDecimal.valueOf(entry.getValue())
                        .divide(BigDecimal.valueOf(totalQuestionCount), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                // 将英文类型转换为中文描述
                String chineseType = getQuestionTypeChineseName(entry.getKey());
                questionTypePercentages.put(chineseType, percentage);
            }
        }
        result.setQuestionTypePercentages(questionTypePercentages);

        // 2. 统计有效答题数量（is_deleted = 0 且 qa_data 不为空）
        List<QuestionnaireDataEntity> dataList = questionnaireDataDao.selectList(
                Wrappers.<QuestionnaireDataEntity>lambdaQuery()
                        .eq(QuestionnaireDataEntity::getGroupsId, groupId)
                        .eq(QuestionnaireDataEntity::getQuestionnaireId, questionnaireId)
                        .isNotNull(QuestionnaireDataEntity::getQaData)
                        .ne(QuestionnaireDataEntity::getQaData, ""));
        Long validAnswerCount = dataList != null ? (long) dataList.size() : 0L;
        result.setValidAnswerCount(validAnswerCount);

        // 3. 计算平均答题时间（分钟）
        BigDecimal totalUseTime = BigDecimal.ZERO;
        int timeCount = 0;
        List<QuestionnaireDataEntity> timeDataList = questionnaireDataDao.selectList(
                Wrappers.<QuestionnaireDataEntity>lambdaQuery()
                        .eq(QuestionnaireDataEntity::getGroupsId, groupId)
                        .eq(QuestionnaireDataEntity::getQuestionnaireId, questionnaireId)
                        .isNotNull(QuestionnaireDataEntity::getUseTime)
                        .gt(QuestionnaireDataEntity::getUseTime, BigDecimal.ZERO));
        if (timeDataList != null) {
            for (QuestionnaireDataEntity data : timeDataList) {
                BigDecimal useTime = data.getUseTime();
                if (useTime != null && useTime.compareTo(BigDecimal.ZERO) > 0) {
                    // 小于或大于零就向前取整一分钟（向上取整到分钟）
                    BigDecimal minutes = useTime.divide(BigDecimal.valueOf(60), 0, BigDecimal.ROUND_CEILING);
                    totalUseTime = totalUseTime.add(minutes);
                    timeCount++;
                }
            }
        }

        BigDecimal averageAnswerTimeMinutes = BigDecimal.ZERO;
        if (timeCount > 0) {
            averageAnswerTimeMinutes = totalUseTime.divide(BigDecimal.valueOf(timeCount), 2, BigDecimal.ROUND_HALF_UP);
        }
        result.setAverageAnswerTimeMinutes(averageAnswerTimeMinutes);

        return result;
    }

    /**
     * 获取题目类型的中文名称
     *
     * @param questionType 题目类型英文名称
     * @return 中文名称
     */
    private String getQuestionTypeChineseName(String questionType) {
        if (questionType == null) {
            return "未知";
        }
        switch (questionType) {
            case "SINGLE_OPTION":
                return "单选";
            case "MULTI_OPTION":
                return "多选";
            case "FILL":
                return "填空";
            case "SHORT_ANSWER":
                return "简答";
            case "SORT":
                return "排序";
            case "FILE":
                return "文件上传";
            case "MEASUREMENT":
                return "量表";
            default:
                return questionType;
        }
    }

    /**
     * 统计每个实验计划的模型字数
     * 通过聊天记录详情表统计模型角色的字数，关联到实验计划并聚合
     *
     * @return 实验计划字数统计列表
     */
    public List<ExperimentWordNumberVo> getExperimentWordNumber() {
        List<ExperimentWordNumberVo> resultList = new ArrayList<>();

        // 通过SQL查询所有word_number不为null且role_type为'MODEL'的聊天记录详情，关联t_chat_history获取element_id，按element_id聚合统计word_number
        List<ElementWordNumberVo> elementWordNumberList = chatHistoryDetailDao.getElementWordNumberSum();

        if (elementWordNumberList == null || elementWordNumberList.isEmpty()) {
            return resultList;
        }

        // 转换为Map便于后续处理
        Map<String, Long> elementWordNumberMap = new HashMap<>();
        for (ElementWordNumberVo vo : elementWordNumberList) {
            if (vo.getElementId() != null && vo.getWordNumber() != null) {
                elementWordNumberMap.put(vo.getElementId(), vo.getWordNumber());
            }
        }



        // 通过t_chat_room_user的user_id找到t_groups_person，再通过experiment_id找到t_experiment_plan
        // Map<experimentId, wordNumber总和>
        Map<Long, Long> experimentWordNumberMap = new HashMap<>();
        // 缓存experimentId到实验计划的映射
        Map<Long, ExperimentPlanEntity> experimentPlanMap = new HashMap<>();

        // 构建返回结果
        for (Map.Entry<Long, Long> entry : experimentWordNumberMap.entrySet()) {
            Long experimentId = entry.getKey();
            Long totalWordNumber = entry.getValue();

            ExperimentPlanEntity experimentPlan = experimentPlanMap.get(experimentId);
            if (experimentPlan != null) {
                ExperimentWordNumberVo vo = new ExperimentWordNumberVo();
                vo.setExperimentCode(experimentPlan.getExperimentCode());
                vo.setExperimentName(experimentPlan.getExperimentName());
                vo.setExperimentTitle(experimentPlan.getExperimentTitle());
                vo.setWordNumber(totalWordNumber);
                resultList.add(vo);
            }
        }

        return resultList;
    }
}
