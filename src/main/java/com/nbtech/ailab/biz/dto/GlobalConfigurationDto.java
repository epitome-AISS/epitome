package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
@Data
@ApiModel(value = "全局配置表")
public class GlobalConfigurationDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "是否启用审核 1启用 0禁用")
	private Integer isEnableReview;

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
	private Integer isDelete;


}