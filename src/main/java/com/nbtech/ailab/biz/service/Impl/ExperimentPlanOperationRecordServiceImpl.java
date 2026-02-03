package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.nbtech.ailab.biz.dao.ExperimentPlanOperationRecordDao;
import com.nbtech.ailab.biz.dto.ExperimentPlanOperationRecordDto;
import com.nbtech.ailab.biz.entity.ExperimentPlanOperationRecordEntity;
import com.nbtech.ailab.biz.service.IExperimentPlanOperationRecordService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 实验更新表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class ExperimentPlanOperationRecordServiceImpl extends CrudServiceImpl<ExperimentPlanOperationRecordDao, ExperimentPlanOperationRecordEntity, ExperimentPlanOperationRecordDto> implements IExperimentPlanOperationRecordService {

    @Override
    public QueryWrapper<ExperimentPlanOperationRecordEntity> getWrapper(ExperimentPlanOperationRecordDto dto){

        QueryWrapper<ExperimentPlanOperationRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }


}