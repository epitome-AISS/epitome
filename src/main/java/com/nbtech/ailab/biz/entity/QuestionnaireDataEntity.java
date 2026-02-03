package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_questionnaire_data")
public class QuestionnaireDataEntity {

    /**
     * 主键id
     */
	private Long id;
    /**
     * 实验组id
     */
	private Long groupsId;
    /**
     * 问卷管理id
     */
	private Long questionnaireId;
    /**
     * 受试者id
     */
	private Long userId;

    /**
     * 回合数
     */
    private Long round;

    /**
     * 数据来源  多人多轮 WORKPROCESS 普通问卷 null
     */
    private String sourceType;
    /**
     * 流程id
     */
    private String processId;
    /**
     * 回答数据
     */
	private String qaData;
    /**
     * 算子id
     */
    private String elementId;
    /**
     * 回答耗时
     */
    private BigDecimal useTime;
    /**
     * 开始时间
     */
	private LocalDateTime startTime;
    /**
     * 结束时间
     */
	private LocalDateTime endTime;
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
     * 是否删除 1为删除 0为未删除
     */
    @TableLogic
    private Integer isDeleted;
}