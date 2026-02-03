package com.nbtech.ailab.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDateTime;


/**
 * @author liHuiRu
 * @date 2023/5/30 13:20
 * @Description: 生产制单导出
 */
@Slf4j
@Data
public class ExcelExportCellHandle implements SheetWriteHandler {

    /**
     * 实验场景
     */
    private String experimentScene;

    /**
     * 实验编号
     */
    private String experimentCode;

    /**
     * 实验名称
     */
    private String experimentName;

    /**
     * 预设实验者人数
     */
    private Integer experimentPersonNumber;

    /**
     * sheet 名称
     */
    private String sheetName;

    /**
     * 实验联系人
     */
    private String experimentContact;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 实验网址
     */
    private String website;

    /**
     * 聊天室开启时间
     */
    private String startTime;




    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        SheetWriteHandler.super.beforeSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheet(sheetName);
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // 设置单元格左对齐
        style.setAlignment(HorizontalAlignment.LEFT);
//        font.setFontName("宋体");
//
        style.setFont(font);
        // 第一列
        Row row = sheet.createRow(0);
        Cell codeTitle = row.createCell(0);
        Cell code = row.createCell(1);
        codeTitle.setCellValue("ExperimentCode:");
        code.setCellValue(experimentCode);
        // 第二列
        Row row2 = sheet.createRow(1);
        Cell nameTitle = row2.createCell(0);
        Cell name = row2.createCell(1);
        nameTitle.setCellValue("ExperimentName:");
        name.setCellValue(experimentName);

        //设置字体样式大小
        codeTitle.setCellStyle(style);
        code.setCellStyle(style);
        nameTitle.setCellStyle(style);
        name.setCellStyle(style);

        // 第三列
        Row row3 = sheet.createRow(2);
        Cell sceneTitle = row3.createCell(0);
        Cell scene = row3.createCell(1);
        sceneTitle.setCellValue("ExperimentScene:");
        scene.setCellValue(experimentScene);

        // 第四列
        Row row31 = sheet.createRow(3);
        Cell startTimeTitle = row31.createCell(0);
        Cell startTimeCell = row31.createCell(1);
        startTimeTitle.setCellValue("ChatRoomStartTime:");
        startTimeCell.setCellValue(startTime);
        startTimeTitle.setCellStyle(style);
        startTimeCell.setCellStyle(style);

        // 第五列
        Row row4 = sheet.createRow(4);
        Cell personNumberTitle = row4.createCell(0);
        Cell personNumber = row4.createCell(1);
        personNumberTitle.setCellValue("ExperimentPersonNumber:");
        personNumber.setCellValue(experimentPersonNumber);

        //设置字体样式大小
        sceneTitle.setCellStyle(style);
        scene.setCellStyle(style);
        personNumberTitle.setCellStyle(style);
        personNumber.setCellStyle(style);

        //第六列
        Row row5 = sheet.createRow(5);
        Cell contactTitle = row5.createCell(0);
        Cell contact = row5.createCell(1);
        contactTitle.setCellValue("ExperimentContact:");
        contact.setCellValue(experimentContact);
        //第七列
        Row row6 = sheet.createRow(6);
        Cell emailTitle = row6.createCell(0);
        Cell email = row6.createCell(1);
        emailTitle.setCellValue("ContactEmail:");
        email.setCellValue(contactEmail);

        contactTitle.setCellStyle(style);
        contact.setCellStyle(style);
        emailTitle.setCellStyle(style);
        email.setCellStyle(style);

        //第八列
        Row row7 = sheet.createRow(7);
        Cell webseitTitle = row7.createCell(0);
        Cell webseit1 = row7.createCell(1);
        webseitTitle.setCellValue("Website:");
        webseit1.setCellValue(website);

        webseitTitle.setCellStyle(style);
        webseit1.setCellStyle(style);

        SheetWriteHandler.super.afterSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }

}
