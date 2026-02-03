package com.nbtech.ailab.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.nbtech.ailab.biz.entity.ChatHistoryEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ChatRecordVo extends ChatHistoryEntity {

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
     * 当前回合数
     */
    private Integer round;
    /**
     * 流程id
     */
    private String processId;
    /**
     * 聊天记录
     */
    private String record;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 记录时间
     */
    private BigDecimal recordTime;
    /**
     * 用户/模型id
     */
    private Long enterTime;
    /**
     * 字数长度
     */
    private Integer wordNumber;

    /**
     * 是否是预览
     */
    private Boolean isDryRun;

    /**
     * 头像
     */
    private String avatar;
}
