package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.QuestionnaireScaleDao;
import com.nbtech.ailab.biz.dto.QuestionnaireScaleDto;
import com.nbtech.ailab.biz.entity.QuestionnaireScaleEntity;
import com.nbtech.ailab.biz.service.IQuestionnaireScaleService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 问卷(量表)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Service
public class QuestionnaireScaleServiceImpl extends CrudServiceImpl<QuestionnaireScaleDao, QuestionnaireScaleEntity, QuestionnaireScaleDto> implements IQuestionnaireScaleService {

    @Override
    public QueryWrapper<QuestionnaireScaleEntity> getWrapper(QuestionnaireScaleDto dto){

        QueryWrapper<QuestionnaireScaleEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}