package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模型对话管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-11
 */
@Data
@TableName("t_model")
public class ModelEntity {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 模型对话名称
     */
    private String modelName;
    /**
     * 基础模型
     */
    private String models;

    /**
     * 标签
     */
    private String tag;

    /**
     * 模型对话头像
     */
    private String modelImageUrl;
    /**
     * 用户头像
     */
    private String userImageUrl;
    /**
     * 问候信息
     */
    private String greetInfo;
    /**
     * 提示词
     */
    private String prompt;
    /**
     * 温度
     */
    private BigDecimal temperature;
    /**
     * 最大token数
     */
    private Long maxTokens;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
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
    /**
     * 对话方式 1单一 2单个随机 3多个随机
     */
    private Integer way;
    /**
     * 模型状态
     */
    private String modelStatus;
    /**
     * 操作流程
     */
    private String workFlow;
    /**
     * 审核是否通过
     */
    private Integer isReview;
    /**
     * 模型对话归属人
     */
    private String attribution;
    /**
     * 归属实验计划id
     */
    private Long experimentPlanId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 是否删除 1为删除 0为未删除
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 故事场景设定
     */
    private String storySetting;
    /**
     * 角色信息设定
     */
    private String roleSetting;
    /**
     * 对话主题
     */
    private String talkingTopic;
    /**
     * 开始话题的角色
     */
    private String startRole;
    /**
     * 受试者扮演的角色
     */
    private String subjectRole;
    /**
     * 模型机器人算子类型 1单一模型对话 2群聊对话 3 智能教育
     */
    private Integer modelBotType;

    /**
     * 响应配置 1是展示 0是不展示
     */
    private Integer responsePrompt;

    /**
     * 响应文本
     */
    private String responseText;

    /**
     * 题目和选项
     */
    private String question;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 老师头像
     */
    private String teacherAvatar;

    /**
     * 学生头像
     */
    private String studentAvatar;

    /**
     * 响应时间
     */
    private BigDecimal responseTime;

    /**
     * 配置
     */
    private String config;
}