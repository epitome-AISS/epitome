package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 聊天记录子表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-07-11
 */
@Data
public class ChatHistoryDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long chatHistoryId;

	private Long userId;

	private String roleType;

	private Object record;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 记录输入时间
	 */
	private Long enterTime;

	/**
	 * 消息的消费状态
	 */
	private Boolean consumptionStatus;
	/**
	 * 头像
	 */
	private String avatar;

	private BigDecimal recordTime;

	private Integer wordNumber;

	private Integer isDeleted;

	private Long creator;

	private LocalDateTime createDate;

	private Long updater;

	private LocalDateTime updateDate;

	private String createName;

	private String updateName;

	/**
	 * 当前回合
	 */
	private Integer round;

	private String processId;

}