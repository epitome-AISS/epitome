package com.nbtech.ailab.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeExcelUtil {

    /**
     * 生成二维码图片
     * @param url 需要生成二维码的链接
     * @param width 二维码宽度
     * @param height 二维码高度
     * @return 二维码图片的BufferedImage对象
     */
    public static BufferedImage generateQRCode(String url, int width, int height) throws WriterException {
        // 配置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 高纠错级别
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // 边距

        // 创建二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        // 转换为BufferedImage
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 将二维码插入到Excel的指定单元格
     * @param workbook Excel工作簿
     * @param sheet 工作表
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @param qrCodeImage 二维码图片
     * @param textUrl 要在单元格中显示的文本链接（可选）
     */
    public static void insertQRCodeToExcel(XSSFWorkbook workbook, XSSFSheet sheet,
                                           int rowIndex, int columnIndex,
                                           BufferedImage qrCodeImage, String textUrl) throws IOException {

        // 设置行高和列宽以适应二维码
        XSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        row.setHeightInPoints(120); // 设置行高
        sheet.setColumnWidth(columnIndex, 20 * 256); // 设置列宽

        // 如果需要在单元格中显示链接文本
        if (textUrl != null && !textUrl.isEmpty()) {
            XSSFCell cell = row.createCell(columnIndex);
            cell.setCellValue(textUrl);
        }

        // 将二维码图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // 将图片添加到工作簿
        int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

        // 创建绘图对象
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();

        // 创建图片锚点，定位图片在单元格中的位置
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(columnIndex);
        anchor.setRow1(rowIndex);
        anchor.setCol2(columnIndex + 1);
        anchor.setRow2(rowIndex + 1);

        // 将图片添加到工作表
        drawing.createPicture(anchor, pictureIndex);
    }

    /**
     * 创建包含二维码的Excel文件
     * @param urls 链接列表
     * @param filePath 保存的Excel文件路径
     */
    public static void createExcelWithQRCodes(String[] urls, String filePath) throws IOException, WriterException {
        // 创建工作簿和工作表
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("二维码");

        // 创建标题行
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("序号");
        headerRow.createCell(1).setCellValue("链接");
        headerRow.createCell(2).setCellValue("二维码");

        // 为每个链接生成二维码并插入Excel
        for (int i = 0; i < urls.length; i++) {
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1); // 序号
            row.createCell(1).setCellValue(urls[i]); // 链接文本

            // 生成二维码
            BufferedImage qrCodeImage = generateQRCode(urls[i], 200, 200);

            // 插入二维码到Excel
            insertQRCodeToExcel(workbook, sheet, i + 1, 2, qrCodeImage, null);
        }

        // 保存Excel文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
