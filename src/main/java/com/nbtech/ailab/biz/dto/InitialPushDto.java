package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Data
@ApiModel(value = "初始化推送数据表")
public class InitialPushDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "算子id")
	private String elementId;

	@ApiModelProperty(value = "用户id")
	private Integer userId;

	@ApiModelProperty(value = "数据")
	private String data;

	@ApiModelProperty(value = "标识推送方的身份")
	private String identity;


}