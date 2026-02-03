package com.nbtech.ailab.biz.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author nber
 */
@Data
@ApiModel(value = "数据包模型对话算子dto")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class ModelInfoDto {
    @ApiModelProperty(value = "用户id")
    @ExcelIgnore
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    @ExcelProperty(value = "userName")
    @ColumnWidth(value = 30)
    private String userName;

    @ApiModelProperty(value = "模型对话算子id")
    @ExcelIgnore
    private String elementId;

    @ApiModelProperty(value = "编号")
    @ExcelProperty(value = "numbering")
    @ColumnWidth(value = 15)
    private Long modelId;

    @ApiModelProperty(value = "模型名")
    @ExcelProperty(value = "model")
    @ColumnWidth(value = 30)
    private String modelName;

    @ApiModelProperty(value = "算子名称")
    @ExcelIgnore
    private String elementName;

    @ApiModelProperty(value = "角色")
    @ExcelProperty(value = "role")
    @ColumnWidth(value = 20)
    private String role;

    @ApiModelProperty(value = "内容")
    @ExcelProperty(value = "content")
    @ColumnWidth(value = 30)
    private String record;

    @ApiModelProperty(value = "字数")
    @ExcelProperty(value = "wordNumber")
    @ColumnWidth(value = 20)
    private String wordNumber;

    @ApiModelProperty(value = "发言时间")
    @ExcelProperty(value = "recordTime")
    @ExcelIgnore
    private BigDecimal recordTime;

    @ApiModelProperty(value = "开始时间")
//    @ExcelProperty(value = "startTime")
//    @ColumnWidth(value = 30)
    @ExcelIgnore
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
//    @ExcelProperty(value = "endTime")
//    @ColumnWidth(value = 30)
    @ExcelIgnore
    private LocalDateTime endTime;

    @ApiModelProperty(value = "耗时(秒)")
    @ExcelProperty(value = "Time(s)")
    @ColumnWidth(value = 20)
    private BigDecimal second;

}
