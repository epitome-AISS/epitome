package com.nbtech.ailab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 文学写作初始化参数
 * 
 * @author system
 */
@Data
@ApiModel("文学写作初始化参数")
public class LiteratureWriteInitParam {

    /**
     * 段落模式：polish（润色）/ shrink（缩写）/ expand（扩写）
     * 只能输入这三个值中的一个
     */
    @NotEmpty(message = "段落模式不能为空")
    @Pattern(regexp = "^(polish|shrink|expand)$", message = "段落模式只能选择polish（润色）、shrink（缩写）或expand（扩写）")
    @ApiModelProperty(value = "段落模式", required = true, allowableValues = "polish,shrink,expand", notes = "polish（润色）/ shrink（缩写）/ expand（扩写）")
    private String segmentMode;
}
