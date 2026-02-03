package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@ApiModel(value = "问卷数据")
public class QuestionnaireDataDto implements Serializable {
    private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "问卷管理id")
	private Long questionnaireId;

	@ApiModelProperty(value = "受试者id")
	private Long userId;

	@ApiModelProperty(value = "回答数据")
	private String qaData;

	@ApiModelProperty(value = "回合数")
	private Long round;

	@ApiModelProperty(value = "数据来源  多人多轮 WORKPROCESS 普通问卷 null")
	private String sourceType;

	@ApiModelProperty(value = "流程id")
	private String processId;

    @ApiModelProperty(value = "算子id")
    private String elementId;

	@ApiModelProperty(value = "开始时间")
	private LocalDateTime startTime;

	@ApiModelProperty(value = "结束时间")
	private LocalDateTime endTime;

	@ApiModelProperty(value = "回答耗时")
	private BigDecimal useTime;

	@ApiModelProperty(value = "创建人")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "更新人")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateDate;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "是否删除 1为删除 0为未删除")
    private Integer isDeleted;
}