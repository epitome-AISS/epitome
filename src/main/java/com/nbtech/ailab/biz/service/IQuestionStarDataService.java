package com.nbtech.ailab.biz.service;

import com.alibaba.fastjson.JSONArray;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireExcelVo;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireVo;
import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.QuestionStarDataDto;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;

import java.util.List;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
public interface IQuestionStarDataService extends CrudService<QuestionStarDataEntity, QuestionStarDataDto> {

    /**
     * 获取所有问卷星的答卷数据
     */
    List<List<QuestionStarDataEntity>> getElementStarData(String processConfig);

    /**
     * 获取这个实验组下所有多人多轮工作流问卷结果
     */
    List<ProcessQuestionnaireVo> getProcessQuestionnaire(Long groupId);

    /**
     * 查询工作流的问卷的答卷数据
     * @param groupId 实验组id
     * @param questionnaireId 问卷id
     * @param elementId 算子id
     * @param processId 流程id
     * @return
     */
    List<ProcessQuestionnaireExcelVo> getProcessData(Long groupId,Long questionnaireId,String elementId,String processId);
}