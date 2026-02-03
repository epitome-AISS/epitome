package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.GlobalConfigurationDao;
import com.nbtech.ailab.biz.dto.GlobalConfigurationDto;
import com.nbtech.ailab.biz.entity.GlobalConfigurationEntity;
import com.nbtech.ailab.biz.service.IGlobalConfigurationService;
import org.springframework.stereotype.Service;

/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
@Service
public class GlobalConfigurationServiceImpl extends CrudServiceImpl<GlobalConfigurationDao, GlobalConfigurationEntity, GlobalConfigurationDto> implements IGlobalConfigurationService {

    @Override
    public QueryWrapper<GlobalConfigurationEntity> getWrapper(GlobalConfigurationDto dto) {

        QueryWrapper<GlobalConfigurationEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Override
    public void enableReview(Integer isEnable) {
        GlobalConfigurationDto globalConfigurationDto = get(1L);
        switch (isEnable) {
            case 1:
                globalConfigurationDto.setIsEnableReview(1);
                break;
            case 0:
                globalConfigurationDto.setIsEnableReview(0);
                break;
            default:
                break;
        }

        update(globalConfigurationDto);
    }
}