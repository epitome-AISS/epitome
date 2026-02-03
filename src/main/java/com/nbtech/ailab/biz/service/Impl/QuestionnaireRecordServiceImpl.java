package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.QuestionnaireRecordDao;
import com.nbtech.ailab.biz.dto.QuestionnaireRecordDto;
import com.nbtech.ailab.biz.entity.QuestionnaireRecordEntity;
import com.nbtech.ailab.biz.service.IQuestionnaireRecordService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 问卷(填空、简答)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Service
public class QuestionnaireRecordServiceImpl extends CrudServiceImpl<QuestionnaireRecordDao, QuestionnaireRecordEntity, QuestionnaireRecordDto> implements IQuestionnaireRecordService {

    @Override
    public QueryWrapper<QuestionnaireRecordEntity> getWrapper(QuestionnaireRecordDto dto){

        QueryWrapper<QuestionnaireRecordEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}