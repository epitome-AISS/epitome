package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
@Data
@ApiModel(value = "实验组信息表")
public class ExperimentMessageDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupId;

	@ApiModelProperty(value = "进行中人数")
	private Long processingNumber;

	@ApiModelProperty(value = "完成人数")
	private Long finishedNumber;

	@ApiModelProperty(value = "累计完成人数")
	private Long sumNumber;

	@ApiModelProperty(value = "实验统计当前日期")
	private LocalDate recordDate;


}