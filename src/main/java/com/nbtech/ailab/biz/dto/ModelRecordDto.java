package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 大模型问答(填空、简答)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@ApiModel(value = "大模型问答(填空、简答)")
public class ModelRecordDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模型问答id")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "算子顺序编号")
	private Integer operatorPrecedence;

	@ApiModelProperty(value = "模型名称")
	private String modelName;

	@ApiModelProperty(value = "问答回合数")
	private Integer roundCount;

	@ApiModelProperty(value = "问题总字数")
	private Integer questionWords;

	@ApiModelProperty(value = "回答总字数")
	private Integer answerWords;

	@ApiModelProperty(value = "回答总耗时")
	private Integer spentTime;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

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