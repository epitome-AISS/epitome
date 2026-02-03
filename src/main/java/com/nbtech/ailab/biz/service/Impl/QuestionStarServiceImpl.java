package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.QuestionStarDao;
import com.nbtech.ailab.biz.dto.QuestionStarDto;
import com.nbtech.ailab.biz.entity.QuestionStarEntity;
import com.nbtech.ailab.biz.service.IQuestionStarService;
import org.springframework.stereotype.Service;

/**
 * 问卷星问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-28
 */
@Service
public class QuestionStarServiceImpl extends CrudServiceImpl<QuestionStarDao, QuestionStarEntity, QuestionStarDto> implements IQuestionStarService {

    @Override
    public QueryWrapper<QuestionStarEntity> getWrapper(QuestionStarDto dto){

        QueryWrapper<QuestionStarEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}