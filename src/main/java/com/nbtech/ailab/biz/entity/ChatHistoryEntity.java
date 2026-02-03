package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 聊天记录主表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-07-11
 */
@Data
@TableName("t_chat_history")
public class ChatHistoryEntity {

    /**
     * 聊天记录主表id
     */
	private Long id;
    /**
     * 实验组id
     */
	private Long groupsId;
    /**
     * 算子id
     */
	private String elementId;
    /**
     * 算子序号
     */
	private Integer elementSort;
    /**
     * 流程id
     */
    private String processId;
    /**
     * 当前回合
     */
    private Integer round;
    /**
     * 删除 0否/1是
     */
    @TableLogic
	private Integer isDeleted;
    /**
     * 
     */
	private Long creator;
    /**
     * 创建时间
     */
	private LocalDateTime createDate;
    /**
     * 
     */
	private Long updater;
    /**
     * 更新时间
     */
	private LocalDateTime updateDate;
    /**
     * 创建人名称
     */
	private String createName;
    /**
     * 创建人名称
     */
	private String updateName;
}