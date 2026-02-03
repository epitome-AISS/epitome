package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实验流程进展表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@TableName("t_experiment_progress")
public class ExperimentProgressEntity {

    /**
     * 进展id
     */
    private Long id;
    /**
     * 实验表id
     */
    private Long experimentId;
    /**
     * 实验组id
     */
    private Long groupsId;
    /**
     * 模型组id
     */
    private Long setId;
    /**
     * 受试者id
     */
    private Long userId;
    /**
     * 算子Id
     */
    private String elementId;
    /**
     * 素材id
     */
    private Long materialId;
    /**
     * 算子序号
     */
    private Integer sequence;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 
     */
    private Long creator;
    /**
     * 制单时间
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
    @TableLogic
    private Integer isDeleted;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 创建人名称
     */
    private String createName;
}