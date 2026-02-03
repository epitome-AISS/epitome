package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 模型收藏分页查询DTO（包含模型信息）
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-12-24
 */
@Data
@ApiModel(value = "模型收藏分页查询DTO")
public class ModelFavoritePageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收藏表id")
    private Long favoriteId;

    @ApiModelProperty(value = "收藏时间")
    private LocalDateTime favoriteTime;

    @ApiModelProperty(value = "模型对话管理id")
    private Long id;

    @ApiModelProperty(value = "模型对话名称")
    private String modelName;

    @ApiModelProperty(value = "基础模型")
    private String models;

    @ApiModelProperty(value = "模型对话头像")
    private String modelImageUrl;

    @ApiModelProperty(value = "用户头像")
    private String userImageUrl;

    @ApiModelProperty(value = "问候信息")
    private String greetInfo;

    @ApiModelProperty(value = "提示词")
    private String prompt;

    @ApiModelProperty(value = "温度")
    private BigDecimal temperature;

    @ApiModelProperty(value = "最大token数")
    private Long maxTokens;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "对话方式 1单一  2 单个随机 3 多个随机")
    private Integer way;

    @ApiModelProperty(value = "模型状态")
    private String modelStatus;

    @ApiModelProperty(value = "操作流程")
    private String workFlow;

    @ApiModelProperty(value = "审核是否通过")
    private Integer isReview;

    @ApiModelProperty(value = "模型对话归属人")
    private String attribution;

    @ApiModelProperty(value = "归属实验计划id")
    private Long experimentPlanId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "是否删除 1为删除 0为未删除")
    private Integer isDelete;

    @ApiModelProperty(value = "故事场景设定")
    private String storySetting;

    @ApiModelProperty(value = "角色信息设定")
    private String roleSetting;

    @ApiModelProperty(value = "对话主题")
    private String talkingTopic;

    @ApiModelProperty(value = "开始话题的角色")
    private String startRole;

    @ApiModelProperty(value = "受试者扮演的角色")
    private String subjectRole;

    @ApiModelProperty(value = "模型机器人算子类型 1单一模型对话 2群聊对话")
    private Integer modelBotType;

    @ApiModelProperty(value = "响应配置")
    private Integer responsePrompt;

    @ApiModelProperty(value = "响应文本 1是展示 0是不展示")
    private String responseText;

    @ApiModelProperty(value = "响应时间")
    private BigDecimal responseTime;

    @ApiModelProperty(value = "题目和选项")
    private String question;

    @ApiModelProperty(value = "题目答案")
    private String answer;

    @ApiModelProperty(value = "老师头像")
    private String teacherAvatar;

    @ApiModelProperty(value = "学生头像")
    private String studentAvatar;

    @ApiModelProperty(value = "配置")
    private String config;

    @ApiModelProperty(value = "是否被当前用户收藏")
    private Boolean isFavorite;
}

