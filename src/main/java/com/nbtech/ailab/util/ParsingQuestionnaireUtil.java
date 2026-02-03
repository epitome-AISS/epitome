package com.nbtech.ailab.util;

import com.alibaba.fastjson.JSON;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.common.QuestionTypeEnum;
import com.nbtech.ailab.constant.QuestionTypeConstant;
import com.nbtech.ailab.vo.AnswerVo;
import com.nbtech.ailab.vo.ChoiceVo;
import com.nbtech.ailab.vo.QuestionDataVo;
import com.nbtech.ailab.vo.QuestionnaireFormatVo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ParsingQuestionnaireUtil {
    /**
     * 解析问卷数据
     * @param entity 答题数据
     * @param questionDatas 问卷原数据
     */
    public static QuestionnaireFormatVo parsingData(QuestionnaireDataEntity entity, List<QuestionDataVo> questionDatas) {
        Long groupId = entity.getGroupsId();
        Long questionnaireId = entity.getQuestionnaireId();

        List<QuestionnaireScaleEntity> questionnaireScaleEntities = new ArrayList<>();
        List<QuestionnaireSelectionEntity> questionnaireSelectionEntities = new ArrayList<>();
        List<QuestionnaireRecordEntity> questionnaireRecordEntities = new ArrayList<>();

        QuestionnaireFormatVo questionnaireFormatVo = new QuestionnaireFormatVo();
        questionnaireFormatVo.setQuestionnaireSelectionEntities(questionnaireSelectionEntities);
        questionnaireFormatVo.setQuestionnaireScaleEntities(questionnaireScaleEntities);
        questionnaireFormatVo.setQuestionnaireRecordEntities(questionnaireRecordEntities);

        String elementId = entity.getElementId();
        List<AnswerVo> answerVos = JSON.parseArray(entity.getQaData(), AnswerVo.class);
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
                                    .questionnaireId(questionnaireId)
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
                                    .questionnaireId(questionnaireId)
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
                                    .questionnaireId(questionnaireId)
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
                                    .questionnaireId(questionnaireId)
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
                                    .questionnaireId(questionnaireId)
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
                                        .questionnaireId(questionnaireId)
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
                                        .questionnaireId(questionnaireId)
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
        return questionnaireFormatVo;
    }

}
