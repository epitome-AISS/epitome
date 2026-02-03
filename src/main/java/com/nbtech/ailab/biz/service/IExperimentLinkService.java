package com.nbtech.ailab.biz.service;

import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.ExperimentLinkDto;
import com.nbtech.ailab.biz.entity.ExperimentLinkEntity;

import java.util.List;

/**
 * 实验链接
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-11
 */
public interface IExperimentLinkService extends CrudService<ExperimentLinkEntity, ExperimentLinkDto> {

    List<String> getList(Long experimentPlanId, Long groupsId);

    List<ExperimentLinkDto> getByGroupId(Long groupsId);
}