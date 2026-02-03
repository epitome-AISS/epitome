package com.nbtech.ailab.util;



import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.nbtech.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;


/**
 * @author nber
 */
@Slf4j
public class FileUtils {

    /**
     * 把压缩文件放到response里面
     * @param file 文件
     * @param response 目标response
     * @param fileName 压缩出来的名称
     * @throws IOException
     */
    public static void downloadZip(File file, HttpServletResponse response,String fileName) throws IOException {
        OutputStream toClient = null;
        try {
            // 以流的形式下载文件。
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 转换为可下载的中文名
            fileName = URLEncoder.encode(fileName, "utf-8");
            fis.close();
            // 清空response
            response.reset();
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            log.error("下载zip压缩包过程发生异常:", e);
        } finally {
            if (toClient != null) {
                try {
                    toClient.close();
                } catch (IOException e) {
                    log.error("zip包下载关流失败:", e);
                }
            }

        }
    }

    /**
     * listData转换为excel的file文件
     * @param data 数据
     * @param clazz excel格式对象
     * @param fileName 文件名字
     * @return
     * @param <T>
     */
    public static <T> File exportReturnToFile(List<T> data, Class<T> clazz, String fileName) {
        File file = null;
        //名称拆分前后缀
        //1.判断文件名是否有扩展
        try {
            if (StringUtils.isBlank(fileName)) {
                file = File.createTempFile(fileName, "");
            } else {
                String[] parts = fileName.split("\\.");
                String extension = "." + parts[parts.length - 1];
                String name = fileName.substring(0, fileName.length() - extension.length() - 1);
                file = File.createTempFile(name, extension);
            }
        } catch (IOException e) {
            log.error("文件生成失败,文件名异常.异常信息:{}", e.getMessage());
            throw new BizException(200,"文件生成失败,文件名异常");
        }
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = defaultStylePolicyPolicy();

        //导出
        EasyExcel.write(file, clazz)
                .registerWriteHandler(horizontalCellStyleStrategy)
                // 去掉默认格式 但是太难看了 算了吧
//                .useDefaultStyle(false)
                .sheet()
                .doWrite(data);
        return file;
    }

    /**
     * listData转换为excel的file文件
     * @head head 自定义不定长的表头
     * @param data 数据
     * @param fileName 文件名字
     * @return
     * @param <T>
     */
    public static <T> File exportReturnToFile(List<List<String>> head, List<List<Object>> data, String fileName) {
        File file = null;
        //名称拆分前后缀
        //1.判断文件名是否有扩展
        try {
            if (StringUtils.isBlank(fileName)) {
                file = File.createTempFile(fileName, "");
            } else {
                String[] parts = fileName.split("\\.");
                String extension = "." + parts[parts.length - 1];
                String name = fileName.substring(0, fileName.length() - extension.length() - 1);
                file = File.createTempFile(name, extension);
            }
        } catch (IOException e) {
            log.error("文件生成失败,文件名异常.异常信息:{}", e.getMessage());
            throw new BizException(200,"文件生成失败,文件名异常");
        }
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = defaultStylePolicyPolicy();
        //导出
        EasyExcel.write(file)
                .head(head)
                .registerWriteHandler(horizontalCellStyleStrategy)
                // 简单设置列宽
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(30))
                // 简单设置头行高和列行高
//                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short)20,(short)20))
                // 去掉默认格式 但是太难看了 算了吧
//                .useDefaultStyle(false)
                .sheet()
                .doWrite(data);
        return file;
    }

    /**
     * 默认样式策略策略
     *
     * @return
     */
    private static HorizontalCellStyleStrategy defaultStylePolicyPolicy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 表头背景色为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置字体为微软雅黑
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置字体为微软雅黑
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

    }



    public static void deletePath(String path) throws IOException {
            // 指定要清空文件的目录路径
            Path directoryPath = Paths.get(path);
            // 检查目录是否存在
            if (!Files.exists(directoryPath)) {
                System.out.println("目录不存在: " + directoryPath);
                return;
            }

            // 检查是否为目录
            if (!Files.isDirectory(directoryPath)) {
                System.out.println(directoryPath + " 不是一个目录");
                return;
            }

            // 删除目录下的所有文件
            Files.walkFileTree(directoryPath, new SimpleFileVisitor<Path>() {
                // 删除文件
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                // 删除目录
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // 如果删除失败，继续删除其他文件
                    return FileVisitResult.CONTINUE;
                }
            });

            // 删除目录本身
            Files.deleteIfExists(directoryPath);
        }


}