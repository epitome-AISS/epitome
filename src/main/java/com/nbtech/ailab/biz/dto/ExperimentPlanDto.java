package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@ApiModel(value = "实验表")
public class ExperimentPlanDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "实验表id")
    private Long id;

    @ApiModelProperty(value = "实验编号")
    private String experimentCode;

    @ApiModelProperty(value = "实验名称")
    private String experimentName;

    @ApiModelProperty(value = "实验概述")
    private String experimentDesc;

    @ApiModelProperty(value = "实验备注")
    private String experimentRemark;

    @ApiModelProperty(value = "实验联系人")
    private String experimentContact;

    @ApiModelProperty(value = "实验标题")
    private String experimentTitle;

    @ApiModelProperty(value = "实验领域")
    private String experimentField;

    @ApiModelProperty(value = "研究假设")
    private String researchHypothesis;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "发布时间开始（范围查询）")
    private LocalDateTime publishTimeStart;

    @ApiModelProperty(value = "发布时间结束（范围查询）")
    private LocalDateTime publishTimeEnd;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "是否通过伦理委员会审核")
    private Boolean ethicsAudit;

    @ApiModelProperty(value = "实验须知")
    private String experimentTag;

    @ApiModelProperty(value = "实验场景")
    private String experimentScene;

    @ApiModelProperty(value = "实验方案")
    private String protocol;

    @ApiModelProperty(value = "实验组数量")
    private Integer groupsNumber;

    @ApiModelProperty(value = "预设实验者人数")
    private Integer experimentPersonNumber;

    @ApiModelProperty(value = "实验状态")
    private String experimentStatus;

    @ApiModelProperty(value = "持有人")
    private String experimentAttribution;

    @ApiModelProperty(value = "智能分析助手")
    private Long intelligentModel;

    @ApiModelProperty(value = "持有状态")
    private String holdingStatus;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "实验开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "实验结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "删除 0否/1是")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "添加的实验组个数")
    private Integer haveGroupNumber;

    @ApiModelProperty("实验方式")
    private String experimentStyle;

    @ApiModelProperty(value = "是否被当前用户收藏")
    private Boolean isFavorite;

    @ApiModelProperty(value = "实验进度")
    private BigDecimal experimentProgress;
}