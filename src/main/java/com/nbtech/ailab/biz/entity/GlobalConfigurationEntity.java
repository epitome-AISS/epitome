package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 全局配置表
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-08
 */
@Data
@TableName("t_global_configuration")
public class GlobalConfigurationEntity {

    /**
     * 主键id
     */
	private Long id;
    /**
     * 是否启用审核 1启用 0禁用
     */
	private Integer isEnableReview;
    /**
     * 创建人
     */
	private Long creator;
    /**
     * 创建时间
     */
	private LocalDateTime createDate;
    /**
     * 更新人
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
     * 更新人名称
     */
	private String updateName;
    /**
     * 是否删除 1为删除 0为未删除
     */
	private Integer isDelete;
}