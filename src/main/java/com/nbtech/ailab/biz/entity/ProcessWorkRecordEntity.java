package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程任务记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-12-24
 */
@Data
@TableName("t_process_work_record")
public class ProcessWorkRecordEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 执行人id
     */
	private Long userId;
    /**
     * 执行人类型
     */
	private String userType;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 执行的第几个回合
     */
	private Integer round;
    /**
     * 实验组id
     */
	private Long groupId;
    /**
     * 算子id
     */
	private String elementId;
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
     * 数据id
     */
	private String flowId;

    /**
     * 是否是预览
     */
    private Integer isPreview;
    /**
     * 任务类型
     */
	private String workType;

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