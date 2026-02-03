package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_experiment_plan")
public class ExperimentPlanEntity {

    /**
     * 实验表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 实验编号
     */
    private String experimentCode;
    /**
     * 实验名称
     */
    private String experimentName;
    /**
     * 实验标题
     */
    private String experimentTitle;
    /**
     * 实验领域
     */
    private String experimentField;
    /**
     * 研究假设
     */
    private String researchHypothesis;
    /**
     * 实验概述
     */
    private String experimentDesc;
    /**
     * 实验备注
     */
    private String experimentRemark;
    /**
     * 实验联系人
     */
    private String experimentContact;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 联系人邮箱
     */
    private String contactEmail;
    /**
     * 是否通过伦理委员会审核
     */
    private Boolean ethicsAudit;
    /**
     * 实验须知
     */
    private String experimentTag;
    /**
     * 实验场景
     */
    private String experimentScene;
    /**
     * 实验组数量
     */
    private Integer groupsNumber;
    /**
     * 预设实验者人数
     */
    private Integer experimentPersonNumber;
    /**
     * 实验状态
     */
    private String experimentStatus;
    /**
     * 智能分析助手
     */
    private Long intelligentModel;
    /**
     * 实验方案
     */
    private String protocol;
    /**
     * 持有人
     */
    private String experimentAttribution;
    /**
     * 持有状态
     */
    private String holdingStatus;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
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
    /**
     * 实验开始时间
     */
    private LocalDateTime startTime;
    /**
     * 实验结束时间
     */
    private LocalDateTime endTime;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 删除 0否/1是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 实验方式
     */
    private String experimentStyle;
}