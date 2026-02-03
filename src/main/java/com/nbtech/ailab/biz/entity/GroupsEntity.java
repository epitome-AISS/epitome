package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实验组表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_groups")
public class GroupsEntity {

    /**
     * 
     */
    private Long id;
    /**
     * 实验组名称
     */
    private String groupsName;

    /**
     * 链接范围
     */
    private String linkScope;
    /**
     * 实验组实验人数
     */
    private Integer groupsPersonNumber;
    /**
     * 制单人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
    /**
     * 制单时间
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
     * 实验id
     */
    private Long experimentId;
    /**
     * 删除 0否/1是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 流程图
     */
    private String processDag;
    /**
     * 流程配置
     */
    private String processConfig;

    /**
     * 场景id
     */
    private Long sceneId;

    /**
     * 包含的干预组
     */
    private String intervention;

    /**
     * 包含的模型组
     */
    private String model;

    /**
     * 包含的数据收集组
     */
    private String dataCollection;

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
     * 秘钥
     */
    private String secret;

    /**
     * 实验完成人数 1/10
     */
    @TableField(exist = false)
    private String completeNumber;

    /**
     * 基础模型id集合
     */
    @TableField(exist = false)
    private String modelIds;
}