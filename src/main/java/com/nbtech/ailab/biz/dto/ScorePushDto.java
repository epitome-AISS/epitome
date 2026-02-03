package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 测评结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-06-05
 */
@Data
@ApiModel(value = "测评结果推送数据")
public class ScorePushDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "算子id")
	private String elementId;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "数据")
	private String data;


}