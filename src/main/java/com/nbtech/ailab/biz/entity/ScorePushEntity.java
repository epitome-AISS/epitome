package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测评结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-06-05
 */
@Data
@TableName("t_score_push")
public class ScorePushEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 算子id
     */
	private String elementId;
    /**
     * 用户id
     */
	private Long userId;
    /**
     * 数据
     */
	private String data;
}