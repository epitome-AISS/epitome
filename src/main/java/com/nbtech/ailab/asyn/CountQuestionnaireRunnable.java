package com.nbtech.ailab.asyn;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.QuestionDto;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.IQuestionnaireRecordService;
import com.nbtech.ailab.biz.service.IQuestionnaireScaleService;
import com.nbtech.ailab.biz.service.IQuestionnaireSelectionService;
import com.nbtech.ailab.common.QuestionTypeEnum;
import com.nbtech.ailab.constant.QuestionTypeConstant;
import com.nbtech.ailab.util.UsePyUtil;
import com.nbtech.ailab.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class CountQuestionnaireRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CountQuestionnaireRunnable.class);
    private final QuestionnaireDataDao questionnaireDataDao;
    private final QuestionnaireDao questionnaireDao;

    private final IQuestionnaireSelectionService questionnaireSelectionService;

    private final IQuestionnaireScaleService questionnaireScaleService;

    private final IQuestionnaireRecordService questionnaireRecordService;

    private final QuestionnaireSelectionDao questionnaireSelectionDao;

    private final QuestionnaireScaleDao questionnaireScaleDao;

    private final QuestionnaireRecordDao questionnaireRecordDao;

    private final GroupsDao groupsDao;

    /**
     * 实验组id
     */
    private final long groupId;

    /**
     * @param groupId 实验组id
     */
    public CountQuestionnaireRunnable(long groupId,
                              QuestionnaireDataDao questionnaireDataDao,
                              QuestionnaireDao questionnaireDao,
                              IQuestionnaireSelectionService questionnaireSelectionService,
                              IQuestionnaireScaleService questionnaireScaleService,
                              IQuestionnaireRecordService questionnaireRecordService,
                              QuestionnaireSelectionDao questionnaireSelectionDao,
                              QuestionnaireScaleDao questionnaireScaleDao,
                              QuestionnaireRecordDao questionnaireRecordDao,
                              GroupsDao groupsDao) {
        this.groupId = groupId;
        this.questionnaireDataDao = questionnaireDataDao;
        this.questionnaireDao = questionnaireDao;
        this.questionnaireSelectionService = questionnaireSelectionService;
        this.questionnaireScaleService = questionnaireScaleService;
        this.questionnaireRecordService = questionnaireRecordService;
        this.questionnaireSelectionDao = questionnaireSelectionDao;
        this.questionnaireScaleDao = questionnaireScaleDao;
        this.questionnaireRecordDao = questionnaireRecordDao;
        this.groupsDao = groupsDao;
    }

    @Override
    public void run() {
        try {
            // 执行结果处理
            saveCollection(groupId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文字类型词云结果分析
     *
     * @param questionnaireId 问卷id
     * @param groupsId        实验组id
     * @param experimentId    实验id
     * @return
     * @throws Exception
     */
    //文字类型的问卷数据分析
    public void createWordCloud(Long questionnaireId, Long groupsId, Long experimentId) throws Exception {
        //获取当前问卷下所有的填空和问答题
        List<QuestionDto> wordQuestions = questionnaireDataDao.getWords(questionnaireId);
        if (!wordQuestions.isEmpty()) {

            for (QuestionDto q : wordQuestions) {
                PyParamVo pyParamVo = new PyParamVo();
                List<String> contexts = questionnaireDao.getWordContextAnswers(groupsId, questionnaireId, q.getQuestionSort());
                // 转成词云的目标文字
                String target = StringUtils.join(contexts, " ");
                pyParamVo.setExperimentId(experimentId);
                pyParamVo.setGroupId(groupsId);
                pyParamVo.setQuestionnaireId(questionnaireId);
                pyParamVo.setTarget(target);
                pyParamVo.setQuestionSort(q.getQuestionSort().toString());
                // 生成问卷的词云图片
                UsePyUtil.createImage(pyParamVo);
            }
        }
    }

    /**
     * 问卷的结果收集
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveCollection(Long groupId) throws Exception {
        // 获取数据收集的问卷信息
        List<Long> questionnaireIdList = questionnaireDataDao.getQuestionnaireId(groupId);
        GroupsEntity groupsEntity = groupsDao.selectById(groupId);

        if (!questionnaireIdList.isEmpty()) {
            // 存在数据收集的算子
            List<QuestionnaireScaleEntity> questionnaireScaleEntities = new ArrayList<>();
            List<QuestionnaireSelectionEntity> questionnaireSelectionEntities = new ArrayList<>();
            List<QuestionnaireRecordEntity> questionnaireRecordEntities = new ArrayList<>();
            // 用来判定这个问卷的词云是否已经生成
            Set<String> uniqueSet = new HashSet<>();
            // 算子级别
            for (Long questionnaireId : questionnaireIdList) {

                QuestionnaireEntity questionnaireEntity = questionnaireDao.selectById(questionnaireId);
                String unique = questionnaireId + "*" + groupId;
                if (!uniqueSet.contains(unique)){
                    // 词云分析 多个算子同一个问卷 所有相同的问卷 的结果统计在一个记录里面
                    try {
                        createWordCloud(questionnaireId, groupId, groupsEntity.getExperimentId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    uniqueSet.add(unique);
                }
                // 问卷 空的问卷就不做数据收集
                if (questionnaireEntity == null) {
                    continue;
                }
                List<QuestionDataVo> questionDatas = getQuestionData(questionnaireEntity.getQuestionnaireData());
                Collections.sort(questionDatas);
                // 算子id
                // 获取这个实验组 这个问卷下的结果
                List<QuestionnaireDataEntity> dataEntities = questionnaireDataDao.groupQuestionData(groupId, questionnaireId);
                // 问卷级别 循环每个人的问卷结果
                try {
                    for (QuestionnaireDataEntity entity : dataEntities) {
                        String elementId = entity.getElementId();
                        List<AnswerVo> answerVos = getAnswerData(entity.getQaData());
                        Collections.sort(answerVos);
                        //  问卷题目级别 循环每个人的问卷的每个题目的结果
                        for (AnswerVo answerVo : answerVos) {
                            int i = answerVo.getAnswerSort();
                            // 问卷数据
                            QuestionDataVo questionDataVo = questionDatas.stream().filter(x -> x.getQuestionSort() == i).findFirst().orElse(null);
                            if (answerVo.getAnswerContext() == null && answerVo.getScale() == null && answerVo.getChoices() == null) {
                                continue;
                            }
                            if (questionDataVo == null) {
                                continue;
                            }
                            switch (questionDataVo.getQuestionType()) {
                                // 简答
                                case QuestionTypeConstant.SHORT_ANSWER:
                                    questionnaireRecordEntities.add(
                                            QuestionnaireRecordEntity.builder()
                                                    .groupsId(groupId)
                                                    .elementId(elementId)
                                                    .questionnaireId(questionnaireEntity.getId())
                                                    .blankType(QuestionTypeEnum.SHORTANSWER.getCode())
                                                    .questionName(questionDatas.get(i).getQuestionName())
                                                    .round(entity.getRound())
                                                    .sourceType(entity.getSourceType())
                                                    .processId(entity.getProcessId())
                                                    .questionSort(i)
                                                    .answer(answerVo.answerContext)
                                                    .userId(entity.getUserId())
                                                    .build());
                                    continue;
                                    // 填空
                                case QuestionTypeConstant.FILL:
                                    questionnaireRecordEntities.add(
                                            QuestionnaireRecordEntity.builder()
                                                    .groupsId(groupId)
                                                    .elementId(elementId)
                                                    .questionnaireId(questionnaireEntity.getId())
                                                    .blankType(QuestionTypeEnum.FILL.getCode())
                                                    .questionName(questionDatas.get(i).getQuestionName())
                                                    .questionSort(i)
                                                    .round(entity.getRound())
                                                    .sourceType(entity.getSourceType())
                                                    .processId(entity.getProcessId())
                                                    .answer(answerVo.answerContext)
                                                    .userId(entity.getUserId())
                                                    .build());
                                    continue;
                                    // 文件上传
                                case QuestionTypeConstant.FILE:
                                    questionnaireRecordEntities.add(
                                            QuestionnaireRecordEntity.builder()
                                                    .groupsId(groupId)
                                                    .elementId(elementId)
                                                    .questionnaireId(questionnaireEntity.getId())
                                                    .blankType(QuestionTypeEnum.FILE.getCode())
                                                    .questionName(questionDatas.get(i).getQuestionName())
                                                    .questionSort(i)
                                                    .round(entity.getRound())
                                                    .sourceType(entity.getSourceType())
                                                    .processId(entity.getProcessId())
                                                    .answer(answerVo.answerContext)
                                                    .userId(entity.getUserId())
                                                    .build());
                                    continue;
                                    // 排序
                                case QuestionTypeConstant.SORT:
                                    questionnaireRecordEntities.add(
                                            QuestionnaireRecordEntity.builder()
                                                    .groupsId(groupId)
                                                    .elementId(elementId)
                                                    .questionnaireId(questionnaireEntity.getId())
                                                    .blankType(QuestionTypeEnum.SORT.getCode())
                                                    .questionName(questionDatas.get(i).getQuestionName())
                                                    .questionSort(i)
                                                    .round(entity.getRound())
                                                    .sourceType(entity.getSourceType())
                                                    .processId(entity.getProcessId())
                                                    .answer(answerVo.answerContext)
                                                    .userId(entity.getUserId())
                                                    .build());
                                    continue;
                                    // 量表
                                case QuestionTypeConstant.MEASUREMENT:
                                    questionnaireScaleEntities.add(
                                            QuestionnaireScaleEntity.builder()
                                                    .groupsId(groupId)
                                                    .elementId(elementId)
                                                    .questionnaireId(questionnaireEntity.getId())
                                                    .questionName(questionDatas.get(i).getQuestionName())
                                                    .questionSort(i)
                                                    .round(entity.getRound())
                                                    .sourceType(entity.getSourceType())
                                                    .processId(entity.getProcessId())
                                                    .scaleGrade(answerVo.scale.getScaleGrade())
                                                    .scaleContext(answerVo.scale.getScaleContext())
                                                    .userId(entity.getUserId())
                                                    .build());
                                    continue;
                                    // 单选
                                case QuestionTypeConstant.SINGLE_OPTION:
                                    for (ChoiceVo vo : answerVo.getChoices()) {
                                        questionnaireSelectionEntities.add(
                                                QuestionnaireSelectionEntity.builder()
                                                        .groupsId(groupId)
                                                        .elementId(elementId)
                                                        .selectType(QuestionTypeEnum.RADIO.getCode())
                                                        .questionnaireId(questionnaireEntity.getId())
                                                        .questionName(questionDatas.get(i).getQuestionName())
                                                        .questionSort(i)
                                                        .round(entity.getRound())
                                                        .sourceType(entity.getSourceType())
                                                        .processId(entity.getProcessId())
                                                        .choiceContext(vo.choiceContext)
                                                        .choiceSequence(vo.getChoiceSort())
                                                        .userId(entity.getUserId())
                                                        .build());
                                    }
                                    continue;
                                    // 多选
                                case QuestionTypeConstant.MULTI_OPTION:
                                    for (ChoiceVo vo : answerVo.getChoices()) {
                                        questionnaireSelectionEntities.add(
                                                QuestionnaireSelectionEntity.builder()
                                                        .groupsId(groupId)
                                                        .elementId(elementId)
                                                        .selectType(QuestionTypeEnum.MULTIPLE.getCode())
                                                        .questionnaireId(questionnaireEntity.getId())
                                                        .questionName(questionDatas.get(i).getQuestionName())
                                                        .questionSort(i)
                                                        .round(entity.getRound())
                                                        .sourceType(entity.getSourceType())
                                                        .processId(entity.getProcessId())
                                                        .choiceContext(vo.choiceContext)
                                                        .choiceSequence(vo.getChoiceSort())
                                                        .userId(entity.getUserId())
                                                        .build());
                                    }
                            }

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            // 清除当前实验组的所有问答记录
            questionnaireSelectionDao.deleteByGroupsId(groupId);
            questionnaireScaleDao.deleteByGroupsId(groupId);
            questionnaireRecordDao.deleteByGroupsId(groupId);

            // 问卷选择题结果添加
            questionnaireSelectionService.insertBatch(questionnaireSelectionEntities);
            // 问卷量表题结果添加
            questionnaireScaleService.insertBatch(questionnaireScaleEntities);
            // 问卷简单填空题结果添加
            questionnaireRecordService.insertBatch(questionnaireRecordEntities);
        }
    }

    /**
     * 问卷题目解析
     */
    List<QuestionDataVo> getQuestionData(String questionData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(questionData, objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionDataVo.class));
    }

    /**
     * 问卷答案解析
     */
    List<AnswerVo> getAnswerData(String answerData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AnswerVo> answerVo = objectMapper.readValue(answerData, objectMapper.getTypeFactory().constructCollectionType(List.class, AnswerVo.class));
        return answerVo;
    }
}
