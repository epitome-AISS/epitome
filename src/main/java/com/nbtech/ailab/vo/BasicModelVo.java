package com.nbtech.ailab.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BasicModelVo {

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

    @ApiModelProperty(value = "url路径")
    private String url;

    @ApiModelProperty(value = "api_key")
    private String apiKey;

    @ApiModelProperty(value = "上下文长度")
    private Integer contextLength;

    @ApiModelProperty(value = "基础模型类型")
    private Integer type;

}
