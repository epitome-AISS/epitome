package com.nbtech.ailab.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工作流结果字段")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class ElementWorkflowExcelVo {

    @ExcelProperty(value = "Workflow Name")
    @ApiModelProperty(value = "工作流名称")
    @ColumnWidth(value = 30)
    private String workflowName;

    @ExcelProperty(value = "User Name")
    @ApiModelProperty(value = "用户名称")
    @ColumnWidth(value = 30)
    private String userName;

    @ExcelProperty(value = "Round")
    @ApiModelProperty(value = "轮次")
    @ColumnWidth(value = 30)
    private Integer round;

    @ExcelProperty(value = "Input Text")
    @ApiModelProperty(value = "输入内容")
    @ColumnWidth(value = 30)
    private String input;

    @ExcelProperty(value = "Output Text")
    @ApiModelProperty(value = "输出内容")
    @ColumnWidth(value = 30)
    private String output;
}
