package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.QuestionnaireDataEntity;
import com.nbtech.ailab.vo.QuestionnaireBoardVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface QuestionnaireDataDao extends BaseMapper<QuestionnaireDataEntity> {

    /**
     * 获取每个实验组每个问卷每个人的问卷结果
     *
     * @param groupsId        实验组id
     * @param questionnaireId 问卷id
     * @return
     */
    List<QuestionnaireDataEntity> groupQuestionData(Long groupsId, Long questionnaireId);

    List<QuestionnaireDataDto> getQuestionnaireDataTotal(Long groupsId, Long questionnaireId);


    //根据答案顺序和问卷获取对应题目
    QuestionDto getBySort(Long id, Long answerSort);

    QuestionInfoDto getByQuestionSort(Long id, Long answerSort);

    //根据答案顺序和问卷获取单选题目 不一定存在
    QuestionDto getSingleQuestion(Long id, Long answerSort);


    QuestionDto getScaleQuestion(Long id, Long answerSort);


    List<AnswerJsonDto> getScaleContexts(Long groupId, Long questionnaireId, Long answerSort);

    List<AnswerJsonDto> getScaleAnswer(Long groupId, Long questionnaireId);

    QuestionInfoDto getQuestionInfo(Long id, Long answerSort);

    List<QuestionnaireDto> getQuestionnaireIdsByGroupId(Long groupId);


    List<Long> getSortsByQuestionnaireId(Long questionnaireId);

    List<QuElementDto> getElementList(Long groupId);

    /**
     * 根据问卷id获取单选题
     *
     * @param questionnaireId 问卷id
     * @return 单选题集合
     */
    List<QuestionDto> getSingles(Long questionnaireId);

    List<QuestionDto> getMultis(Long questionnaireId);

    List<QuestionDto> getWords(Long questionnaireId);

    List<QuestionDto> getSort(Long questionnaireId);

    /**
     * 获取实验组下所有答题问卷数据
     * @param groupId
     * @return
     */
    List<Long> getQuestionnaireId(Long groupId);


}