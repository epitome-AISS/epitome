package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
@Data
@ApiModel(value = "问卷星的问卷答题结果")
public class QuestionStarDataDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "问卷id")
	private Long activity;

	@ApiModelProperty(value = "问卷名称")
	private String name;

	@ApiModelProperty(value = "答题人ip地址")
	private String ipaddress;

	@ApiModelProperty(value = "省份")
	private String province;

	@ApiModelProperty(value = "题目相关信息")
	private String questionTitle;

	@ApiModelProperty(value = "题目答题结果")
	private String questionData;

	@ApiModelProperty(value = "城市")
	private String city;

	@ApiModelProperty(value = "下标")
	private Integer indexDesc;

	@ApiModelProperty(value = "参与者id")
	private String joinid;

	@ApiModelProperty(value = "所用时间")
	private String timetaken;

	@ApiModelProperty(value = "提交时间")
	private LocalDateTime submittime;

	@ApiModelProperty(value = "额外数据")
	private String sojumpparm;

	@ApiModelProperty(value = "总分")
	private Integer totalvalue;

	@ApiModelProperty(value = "签名sign=sha1(activity+index+推送密钥)")
	private Integer sign;

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