package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
@Data
@ApiModel(value = "素材管理")
public class MaterialDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "素材管理id")
    private Long id;

    @ApiModelProperty(value = "元素类型")
    private String materialType;

    @ApiModelProperty(value = "素材状态")
    private String materialStatus;

    @ApiModelProperty(value = "素材名称")
    private String materialName;

    @ApiModelProperty(value = "素材数据")
    private String materialData;

    @ApiModelProperty(value = "归属人")
    private String materialAttribution;

    @ApiModelProperty(value = "归属实验计划id", required = true)
    private Long experimentPlanId;

    @ApiModelProperty(value = "实验计划名称")
    private String experimentPlanName;

    @ApiModelProperty(value = "实验计划标题")
    private String experimentPlanTitle;

    @ApiModelProperty(value = "标签")
    private String tag;

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

    @ApiModelProperty(value = "文件url")
    private String url;

    @ApiModelProperty(value = "流程操作")
    private String workFlow;

    @ApiModelProperty(value = "审核是否通过")
    private Integer isReview;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "是否删除 1为删除 0为未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "是否被当前用户收藏")
    private Boolean isFavorite;

}