package com.nbtech.ailab.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实验字数统计VO
 *
 * @author nber
 */
@Data
@ApiModel("实验字数统计VO")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class ExperimentWordNumberVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "实验编号", index = 0)
    @ApiModelProperty(value = "实验编号")
    @ColumnWidth(value = 20)
    private String experimentCode;

    @ExcelProperty(value = "实验名称", index = 1)
    @ApiModelProperty(value = "实验名称")
    @ColumnWidth(value = 30)
    private String experimentName;

    @ExcelProperty(value = "实验标题", index = 2)
    @ApiModelProperty(value = "实验标题")
    @ColumnWidth(value = 30)
    private String experimentTitle;

    @ExcelProperty(value = "总字数", index = 3)
    @ApiModelProperty(value = "总字数")
    @ColumnWidth(value = 15)
    private Long wordNumber;
}
