package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 实验链接
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-11
 */
@Data
@ApiModel(value = "实验链接")
public class ExperimentLinkDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "实验计划id")
	private Long experimentPlanId;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "链接名称")
	private String linkName;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateDate;

	@ApiModelProperty(value = "删除 0否/1是")
	private Integer isDeleted;

	@ApiModelProperty(value = "创建人名称")
	private String createName;

	@ApiModelProperty(value = "更新人名称")
	private String updateName;


}