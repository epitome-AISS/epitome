package com.nbtech.ailab.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ImageWriteHandler implements SheetWriteHandler {
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        // 获取数据行数
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 8; i <= lastRowNum; i++) { // 从第8行开始，因为前面有标题
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(2); // 获取第3列的单元格（二维码列）
                if (cell != null) {
                    try {
                        // 获取单元格的值
                        byte[] imageBytes = null;
                        if (cell instanceof XSSFCell) {
                            XSSFCell xssfCell = (XSSFCell) cell;
                            if (xssfCell.getCellType() == CellType.STRING) {
                                String value = xssfCell.getStringCellValue();
                                if (value != null && !value.isEmpty()) {
                                    imageBytes = value.getBytes();
                                }
                            }
                        }

                        if (imageBytes != null && imageBytes.length > 0) {
                            // 创建图片
                            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                            CreationHelper helper = workbook.getCreationHelper();
                            Drawing<?> drawing = sheet.createDrawingPatriarch();

                            // 创建锚点
                            ClientAnchor anchor = helper.createClientAnchor();
                            anchor.setCol1(2); // 从第3列开始
                            anchor.setRow1(i); // 当前行
                            anchor.setCol2(3); // 到第4列
                            anchor.setRow2(i + 1); // 到下一行

                            // 添加图片
                            drawing.createPicture(anchor, pictureIdx);

                            // 清除单元格内容
                            cell.setBlank();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
