package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 聊天记录子表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-07-11
 */
@Data
@TableName("t_chat_history_detail")
public class ChatHistoryDetailEntity {

    /**
     * 聊天记录子表id
     */
    private Long id;
    /**
     * 聊天记录主表id
     */
    private Long chatHistoryId;
    /**
     * 用户/模型id
     */
    private Long userId;
    /**
     * 用户类型
     */
    private String roleType;
    /**
     * 聊天记录
     */
    private Object record;

    /**
     * 记录输入时间
     */
    private Long enterTime;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 记录时间
     */
    private BigDecimal recordTime;
    /**
     * 字数长度
     */
    private Integer wordNumber;

    /**
     * 消息的消费状态
     */
    private Boolean consumptionStatus;
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