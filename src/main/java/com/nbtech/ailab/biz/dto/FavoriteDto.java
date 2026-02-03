package com.nbtech.ailab.biz.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.nbtech.ailab.common.FavoriteTypeEnum;
import com.nbtech.ailab.config.FavoriteTypeEnumTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Data
@ApiModel(value = "收藏表")
public class FavoriteDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @TableField(value = "收藏类型")
    private String favoriteType;

    @ApiModelProperty(value = "收藏目标id（实验计划id、素材id或智能体id）")
    private Long targetId;

    @ApiModelProperty(value = "收藏用户id")
    private Long userId;

    @ApiModelProperty(value = "收藏时间")
    private LocalDateTime favoriteTime;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "删除 0否/1是")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建人姓名")
    private String createName;

    @ApiModelProperty(value = "更新人姓名")
    private String updateName;
}

