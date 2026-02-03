package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实验更新表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_experiment_plan_operation_record")
public class ExperimentPlanOperationRecordEntity {

    /**
     * 实验更新id
     */
//    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
	private Long creator;
    /**
     * 实验表id
     */
	private Long experimentId;
    /**
     * 删除 0否/1是
     */
    @TableLogic
	private Integer isDeleted;
    /**
     * 操作类型
     */
	private String operateType;
    /**
     * 操作说明
     */
	private String operateExplaination;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateDate;
    /**
     * 更新人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;
}