package com.nbtech.ailab.biz.service;

import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.ExperimentMessageDto;
import com.nbtech.ailab.biz.entity.ExperimentMessageEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
public interface IExperimentMessageService extends CrudService<ExperimentMessageEntity, ExperimentMessageDto> {
    List<ExperimentMessageDto> getByGroupId(Long groupId);

    ExperimentMessageDto getByDate(Long groupId, LocalDate date);

    /**
     * 生成实验组的完成结果
     *
     * @param groupId 实验组id
     * @param currentDay 记录生成的日期
     */
    ExperimentMessageDto getExperimentMessageDto(Long groupId, LocalDate currentDay);

}