package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 问卷星问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-28
 */
@Data
@ApiModel(value = "问卷星问卷数据")
public class QuestionStarDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "问卷编号")
	private String activityId;

	@ApiModelProperty(value = "问卷标题")
	private String activityName;

	@ApiModelProperty(value = "问卷访问域名")
	private String activityDomain;

	@ApiModelProperty(value = "PC端链接")
	private String activityPcUrl;

	@ApiModelProperty(value = "h5链接")
	private String activityH5Url;

	@ApiModelProperty(value = "AES 加密的问卷")
	private String content;

	@ApiModelProperty(value = "问卷数据json")
	private String data;

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