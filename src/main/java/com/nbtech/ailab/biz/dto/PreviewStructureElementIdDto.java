package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 预览多人多轮工作流的预算子id
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-29
 */
@Data
@ApiModel(value = "预览多人多轮工作流的预算子id")
public class PreviewStructureElementIdDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "多人多轮结构表id")
	private Long structureId;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "算子id")
	private String elementId;

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

	@ApiModelProperty(value = "删除 0否/1是")
	private Integer isDeleted;


}