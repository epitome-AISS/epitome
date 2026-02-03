package com.nbtech.ailab.biz.service;

import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.GlobalConfigurationDto;
import com.nbtech.ailab.biz.entity.GlobalConfigurationEntity;

/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
public interface IGlobalConfigurationService extends CrudService<GlobalConfigurationEntity, GlobalConfigurationDto> {

    void enableReview(Integer isEnable);
}