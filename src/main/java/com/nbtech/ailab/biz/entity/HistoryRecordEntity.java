package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模型问答历史记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Data
@TableName("t_history_record")
public class HistoryRecordEntity {

    /**
     * 历史记录id
     */
	private Long id;
    /**
     * 历史id
     */
	private Long historyId;
    /**
     * 记录
     */
	private Object record;
    /**
     * 角色
     */
    private String role;
    /**
     * 记录时长
     */
	private BigDecimal recordTime;
    /**
     * 字数长度
     */
	private Long wordNumber;

    /**
     * 智能教育配置项
     */
    private String additionInfo;
    /**
     * 
     */
	private Long creator;
    /**
     * 制单时间
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
     * 删除 0否/1是
     */
    @TableLogic
	private Integer isDeleted;
    /**
     * 更新人名称
     */
	private String updateName;
    /**
     * 创建人名称
     */
	private String createName;
}