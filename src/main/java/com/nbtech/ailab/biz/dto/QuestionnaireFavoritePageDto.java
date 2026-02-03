package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问卷收藏分页查询DTO（包含问卷信息）
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Data
@ApiModel(value = "问卷收藏分页查询DTO")
public class QuestionnaireFavoritePageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏表id")
    private Long favoriteId;

    @ApiModelProperty(value = "收藏时间")
    private LocalDateTime favoriteTime;

    @ApiModelProperty(value = "问卷管理id")
    private Long id;

    @ApiModelProperty(value = "问卷名称")
    private String questionnaireName;

    @ApiModelProperty(value = "问卷数据")
    private String questionnaireData;

    @ApiModelProperty(value = "问卷描述")
    private String questionnaireDesc;

    @ApiModelProperty(value = "归属人")
    private String questionnaireAttribution;

    @ApiModelProperty(value = "归属实验计划id")
    private Long experimentPlanId;

    @ApiModelProperty(value = "是否需要计时器")
    private Boolean needTimer;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "问卷状态")
    private String status;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "流程操作")
    private String workFlow;

    @ApiModelProperty(value = "审核是否通过")
    private Integer isReview;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "问卷信息")
    private Object modelInfo;

    @ApiModelProperty(value = "是否删除 1为删除 0为未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "算子id")
    private String elementId;

    @ApiModelProperty(value = "是否被当前用户收藏")
    private Boolean isFavorite;
}

