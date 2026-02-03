package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 素材管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-07
 */
@Data
@TableName("t_material")
public class MaterialEntity {
    /**
     * 素材管理id
     */
    private Long id;
    /**
     * 元素类型
     */
    private String materialType;
    /**
     * 素材状态
     */
    private String materialStatus;
    /**
     * 素材名称
     */
    private String materialName;
    /**
     * 素材数据
     */
    private String materialData;
    /**
     * 归属人
     */
    private String materialAttribution;
    /**
     * 归属实验计划id
     */
    private Long experimentPlanId;
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
     * 更新人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;
    /**
     * 文件url
     */
    private String url;
    /**
     * 流程操作
     */
    private String workFlow;

    /**
     * 标签
     */
    private String tag;
    /**
     * 审核是否通过
     */
    private Integer isReview;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 是否删除 1为删除 0为未删除
     */
    @TableLogic
    private Integer isDelete;
}