package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@Data
@TableName("t_basic_model")
public class BasicModelEntity {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 基础模型名称
     */
    private String name;
    /**
     * 中文显示名
     */
    private String chineseName;

    /**
     * 基础模型类型
     */
    private String modelType;
    /**
     * 模态 1: 纯语言模型 2: 视觉语言模型
     */
    private Integer type;
    /**
     * 英文名
     */
    private String englishName;
    /**
     * 中文描述
     */
    private String chineseDesc;
    /**
     * 英文描述
     */
    private String englishDesc;
    /**
     * 持有人
     */
    private String basicModelAttribution;

    /**
     * url路径
     */
    private String url;
    /**
     * api_key
     */
    private String apiKey;
    /**
     * 持有人id
     */
    private Long userId;
    /**
     * 使用状态
     */
    private String useStatus;
    /**
     * 可用状态 AVAILABLE-可用 UNAVAILABLE-不可用
     */
    private String availableStatus;
    /**
     * 删除 0否/1是
     */
    @TableLogic
    private Integer isDeleted;
    /**
     *
     */
    private Long creator;
    /**
     * 创建时间
     */
    private LocalDateTime createDate;
    /**
     *
     */
    private Long updater;

    /**
     * 长下文长度
     */
    private Integer contextLength;
    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人名称
     */
    private String updateName;
}