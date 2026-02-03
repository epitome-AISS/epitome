package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 实验流程进展表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@ApiModel(value = "实验流程进展表")
@Accessors(chain = true)
public class ExperimentProgressDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "进展id")
	private Long id;

	@ApiModelProperty(value = "实验表id")
	private Long experimentId;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "受试者id")
	private Long userId;

	@ApiModelProperty(value = "算子id")
	private String elementId;

    @ApiModelProperty(value = "算子序号")
    private Integer sequence;

	@ApiModelProperty(value = "素材id")
	private Long materialId;

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "模型组id")
    private Long setId;

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