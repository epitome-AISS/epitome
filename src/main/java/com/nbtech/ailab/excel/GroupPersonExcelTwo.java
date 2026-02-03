package com.nbtech.ailab.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@ContentRowHeight(90)
@HeadRowHeight(value = 20)
@ColumnWidth(100 / 8)
public class GroupPersonExcelTwo {

    @ExcelProperty(value = "QRCode")
    @ColumnWidth(value = 20)
    private byte[] qrCode;

    @ExcelProperty(value = "ExperimentLink")
    @ColumnWidth(value = 20)
    private String experimentLink;
}
