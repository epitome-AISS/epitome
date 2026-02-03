package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireExcelVo;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireVo;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
@Mapper
public interface QuestionStarDataDao extends BaseMapper<QuestionStarDataEntity> {

    /**
     * 通过算子id获取问卷星答题结果
     * @param elementId 算子id
     * @return
     */
    List<QuestionStarDataEntity> getQuestionStarList(String elementId);

    /**
     * 获取实验组下多人多轮的所有问卷信息
     */
    List<ProcessQuestionnaireVo> getProcessQuestionnaire(Long groupId);

    /**
     * 获取这个流程问卷数据的答卷结果
     * @param groupId 实验组id
     * @param questionnaireId 问卷id
     * @param elementId 算子id
     * @param processId 流程id
     * @return
     */
    List<ProcessQuestionnaireExcelVo> getProcessData(Long groupId, Long questionnaireId, String elementId, String processId);
}