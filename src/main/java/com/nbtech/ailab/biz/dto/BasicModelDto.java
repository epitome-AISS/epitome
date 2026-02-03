package com.nbtech.ailab.biz.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@Data
@ApiModel(value = "基础模型表")
public class BasicModelDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "基础模型名称")
    private String name;

    @ApiModelProperty(value = "中文显示名")
    private String chineseName;

    @ApiModelProperty(value = "英文名")
    private String englishName;

    @ApiModelProperty(value = "中文描述")
    private String chineseDesc;

    @ApiModelProperty(value = "英文描述")
    private String englishDesc;

    @ApiModelProperty(value = "持有人")
    private String basicModelAttribution;

    @ApiModelProperty(value = "持有人id")
    private Long userId;

    @ApiModelProperty(value = "使用状态")
    private String useStatus;

    @ApiModelProperty(value = "可用状态 AVAILABLE-可用 UNAVAILABLE-不可用")
    private String availableStatus;

    @ApiModelProperty(value = "url路径")
    private String url;

    @ApiModelProperty(value = "api_key")
    private String apiKey;

    @ApiModelProperty(value = "上下文长度")
    private Integer contextLength;

    @ApiModelProperty(value = "基础模型类型")
    private String modelType;

    @ApiModelProperty(value = "1: 纯语言模型 2: 视觉语言模型")
    private Integer type;

    @TableLogic
    @ApiModelProperty(value = "删除 0否/1是")
    private Integer isDeleted;

    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;


}