package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.QuestionnaireSelectionDao;
import com.nbtech.ailab.biz.dto.QuestionnaireSelectionDto;
import com.nbtech.ailab.biz.entity.QuestionnaireSelectionEntity;
import com.nbtech.ailab.biz.service.IQuestionnaireSelectionService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 问卷(选择题)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Service
public class QuestionnaireSelectionServiceImpl extends CrudServiceImpl<QuestionnaireSelectionDao, QuestionnaireSelectionEntity, QuestionnaireSelectionDto> implements IQuestionnaireSelectionService {

    @Override
    public QueryWrapper<QuestionnaireSelectionEntity> getWrapper(QuestionnaireSelectionDto dto){

        QueryWrapper<QuestionnaireSelectionEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}