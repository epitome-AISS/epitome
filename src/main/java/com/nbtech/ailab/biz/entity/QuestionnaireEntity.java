package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问卷管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_questionnaire")
public class QuestionnaireEntity {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 问卷名称
     */
    private String questionnaireName;
    /**
     * 问卷数据
     */
    private String questionnaireData;
    /**
     * 问卷描述
     */
    private String questionnaireDesc;
    /**
     * 归属人
     */
    private String questionnaireAttribution;

    /**
     * 归属实验计划id
     */
    private Long experimentPlanId;

    /**
     * 是否需要计时器
     */
    private Boolean needTimer;

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
     * 问卷状态
     */
    private String status;
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
     * 流程操作
     */
    private String workFlow;
    /**
     * 审核是否通过
     */
    private Integer isReview;

    /**
     * 问卷信息
     */
    @TableField(exist = false)
    private Object modelInfo;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 是否删除 1为删除 0为未删除
     */
    @TableLogic
    private Integer isDeleted;
}