package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.ExperimentProgressDao;
import com.nbtech.ailab.biz.dto.ExperimentProgressDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.entity.ExperimentProgressEntity;
import com.nbtech.ailab.biz.service.IExperimentProgressService;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 实验流程进展表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Service
public class ExperimentProgressServiceImpl extends CrudServiceImpl<ExperimentProgressDao, ExperimentProgressEntity, ExperimentProgressDto> implements IExperimentProgressService {

    @Override
    public QueryWrapper<ExperimentProgressEntity> getWrapper(ExperimentProgressDto dto){
        QueryWrapper<ExperimentProgressEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

}