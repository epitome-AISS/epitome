package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实验链接
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-11
 */
@Data
@TableName("t_experiment_link")
public class ExperimentLinkEntity {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 实验计划id
     */
    private Long experimentPlanId;
    /**
     * 实验组id
     */
    private Long groupsId;
    /**
     * 链接名称
     */
    private String linkName;
    /**
     * 
     */
    private Long creator;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     * 
     */
    private Long updater;
    /**
     * 更新时间
     */
    private LocalDateTime updateDate;
    /**
     * 删除 0否/1是
     */
    private Integer isDeleted;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 更新人名称
     */
    private String updateName;
}