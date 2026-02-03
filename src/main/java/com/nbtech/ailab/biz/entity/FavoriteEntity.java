package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.nbtech.ailab.common.FavoriteTypeEnum;
import com.nbtech.ailab.config.FavoriteTypeEnumTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Data
@TableName("t_favorite")
public class FavoriteEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收藏类型（EXPERIMENT_PLAN-实验计划，MATERIAL-素材，AGENT-智能体）
     */
    @TableField(value = "favorite_type", typeHandler = FavoriteTypeEnumTypeHandler.class)
    private FavoriteTypeEnum favoriteType;

    /**
     * 收藏目标id（实验计划id、素材id或智能体id）
     */
    private Long targetId;

    /**
     * 收藏用户id
     */
    private Long userId;

    /**
     * 收藏时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime favoriteTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDate;

    /**
     * 删除 0否/1是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    /**
     * 更新人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;
}
