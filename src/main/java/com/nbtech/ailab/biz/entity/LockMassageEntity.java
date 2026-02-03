package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 锁住记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-12-27
 */
@Data
@TableName("t_lock_massage")
public class LockMassageEntity {

    /**
     * 主键
     */
    private Long id;
    /**
     * 实验组id
     */
    private Long groupId;
    /**
     * 算子id
     */
    private String elementId;
    /**
     * 流程id
     */
    private String processId;
    /**
     * 聊天室id
     */
    private Long roomId;
    /**
     * 第几轮次
     */
    private Integer round;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否是预览
     */
    private Integer isPreview;

    /**
     * 锁住的原因
     */
    private String lockReason;

    /**
     * 任务id
     */
    private String workId;
    /**
     * 是否打开状态(是 lock 否 unlock）
     */
    private Boolean status;
    /**
     *
     */
    @TableLogic
    private Integer isDeleted;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     *
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
}