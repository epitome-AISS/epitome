package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 问卷(量表)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@ApiModel(value = "问卷(量表)")
public class QuestionnaireScaleDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "量表id")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupsId;

	@ApiModelProperty(value = "算子顺序编号")
	private Integer operatorPrecedence;

	@ApiModelProperty(value = "问卷id")
	private Long questionnaireId;

	@ApiModelProperty(value = "回合数")
	private Long round;

	@ApiModelProperty(value = "数据来源  多人多轮 WORKPROCESS 普通问卷 null")
	private String sourceType;

    @ApiModelProperty(value = "题目名称")
    private String questionName;

	@ApiModelProperty(value = "工作流id")
	private String processId;

    @ApiModelProperty(value = "题目序号")
    private Integer questionSort;

	@ApiModelProperty(value = "量表等级")
	private Integer scaleGrade;

    @ApiModelProperty(value = "量表内容")
    private String scaleContext;

	@ApiModelProperty(value = "做题耗时")
	private Integer spentTime;

    @ApiModelProperty(value = "算子id")
    private String elementId;

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