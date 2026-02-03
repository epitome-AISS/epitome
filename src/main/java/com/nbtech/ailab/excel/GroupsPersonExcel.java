package com.nbtech.ailab.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;


/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-26
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
@ColumnWidth(100 / 8)
public class GroupsPersonExcel {

    @ExcelProperty(value = "Experiment Code")
    @ColumnWidth(value = 25)
    private String experimentCode;
    @ExcelProperty(value = "Experiment Name")
    @ColumnWidth(value = 20)
    private String experimentName;
    @ExcelProperty(value = "Experimental Group Name")
    @ColumnWidth(value = 30)
    private String groupsName;
    @ExcelProperty(value = "UserName")
    @ColumnWidth(value = 30)
    private String userName;
    @ExcelProperty(value = "Password")
    @ColumnWidth(value = 20)
    private String password;
}