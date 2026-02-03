package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
@Data
@TableName("t_question_star_data")
public class QuestionStarDataEntity implements Serializable {

    /**
     * 主键
     */
	private Long id;
    /**
     * 问卷id
     */
	private String activity;
    /**
     * 问卷名称
     */
	private String name;
    /**
     * 答题人ip地址
     */
	private String ipaddress;
    /**
     * 省份
     */
	private String province;
    /**
     * 题目相关信息
     */
	private String questionTitle;

    /**
     * 题目答题结果
     */
    private String questionData;
    /**
     * 城市
     */
	private String city;
    /**
     * 下标
     */
	private String indexDesc;
    /**
     * 参与者id
     */
	private String joinid;
    /**
     * 所用时间
     */
	private String timetaken;
    /**
     * 提交时间
     */
	private String submittime;

    /**
     * 额外数据
     */
    private String sojumpparm;
    /**
     * 总分
     */
	private String totalvalue;
    /**
     * 签名sign=sha1(activity+index+推送密钥)
     */
	private String sign;
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

    @TableField(exist = false)
    private String userName;
}