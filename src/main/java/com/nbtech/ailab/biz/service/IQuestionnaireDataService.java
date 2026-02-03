package com.nbtech.ailab.biz.service;

import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.QuestionnaireDataDto;
import com.nbtech.ailab.biz.entity.QuestionnaireDataEntity;

import java.util.List;

/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface IQuestionnaireDataService extends CrudService<QuestionnaireDataEntity, QuestionnaireDataDto> {
    List<Long> getUserIds(Long groupId, Long questionnaireId);


}