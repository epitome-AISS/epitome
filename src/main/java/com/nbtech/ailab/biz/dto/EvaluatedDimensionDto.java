package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 受测用户维度
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@ApiModel(value = "受测用户维度")
public class EvaluatedDimensionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "地域")
	private String region;

	@ApiModelProperty(value = "人数")
	private Integer personnelCount;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "制单时间")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateDate;

	@ApiModelProperty(value = "删除 0否/1是")
	private Integer isDeleted;

	@ApiModelProperty(value = "更新人名称")
	private String updateName;

	@ApiModelProperty(value = "创建人名称")
	private String createName;

}