package com.nbtech.ailab.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;


@Data
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
@ColumnWidth(100 / 8)
public class MaterialGroupListDto {

    @ExcelProperty(value = "userName")
    @ColumnWidth(value = 25)
    private String username;

    @ExcelProperty(value = "materialId")
    @ColumnWidth(value = 25)
    private Long materialId;

    @ExcelProperty(value = "materialName")
    @ColumnWidth(value = 25)
    private String materialName;

    @ExcelProperty(value = "data")
    @ColumnWidth(value = 25)
    private String data;
}
