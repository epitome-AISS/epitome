package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 问卷星问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-28
 */
@Data
@TableName("t_question_star")
public class QuestionStarEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 问卷编号
     */
	private String activityId;
    /**
     * 问卷标题
     */
	private String activityName;
    /**
     * 问卷访问域名
     */
	private String activityDomain;
    /**
     * PC端链接
     */
	private String activityPcUrl;

    /**
     * h5链接
     */
    private String activityH5Url;
    /**
     * AES 加密的问卷
     */
	private String content;
    /**
     * 问卷数据json
     */
	private String data;
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