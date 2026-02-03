package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程聊天室输入
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-01-15
 */
@Data
@TableName("t_process_chat")
public class ProcessChatEntity {

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
     * 算子序号
     */
    private Integer elementSort;
    /**
     * 聊天室id
     */
	private Long roomId;
    /**
     * 流程id
     */
	private String processId;
    /**
     * 任务id
     */
	private String workId;
    /**
     * 任务类型
     */
    private String workType;
    /**
     * 角色名称
     */
	private String roleName;
    /**
     * 用户id
     */
	private Long userId;
    /**
     * 第几回合
     */
	private Integer round;
    /**
     * 环境变量名称
     */
    private String envName;
    /**
     * 聊天信息
     */
	private String chatMessage;
    /**
     * 是否已经展示
     */
	private Boolean displayStatus;

    /**
     * 是否是预览
     */
    private Integer isPreview;
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