package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 模型问答历史
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Data
@ApiModel(value = "模型问答历史")
public class ModelHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "历史id")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "算子id")
	private String elemenId;

	@ApiModelProperty(value = "模型名称")
	private String modelName;
	/**
	 * 基础模型id
	 */
	private Long modelId;

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