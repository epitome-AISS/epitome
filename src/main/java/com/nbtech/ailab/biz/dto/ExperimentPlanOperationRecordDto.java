package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 实验更新表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@ApiModel(value = "实验更新表")
public class ExperimentPlanOperationRecordDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "实验更新id")
	private Long id;

	@ApiModelProperty(value = "创建人")
	private Long creator;

	@ApiModelProperty(value = "实验表id")
	private Long experimentId;

	@ApiModelProperty(value = "删除 0否/1是")
	private Integer isDeleted;

	@ApiModelProperty(value = "操作类型")
	private String operateType;

	@ApiModelProperty(value = "操作说明")
	private String operateExplaination;

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

    /**
     * 强制发布 false 不需要强制发布 true 需要强制发布
     */
//    @ApiModelProperty(value = "强制发布")
    public Boolean compulsory;

}