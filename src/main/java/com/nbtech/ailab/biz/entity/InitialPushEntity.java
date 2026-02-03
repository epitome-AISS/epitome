package com.nbtech.ailab.biz.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Data
@TableName("t_initial_push")
public class InitialPushEntity {

    /**
     * 主键
     */
    @ExcelIgnore
	private Long id;
    /**
     * 算子id
     */
    @ExcelIgnore
	private String elementId;
    /**
     * 用户id
     */
    @ExcelIgnore
	private Long userId;

    @ExcelIgnore
    private String identity;

    @TableField(exist = false)
    @ExcelProperty(value = "user name")
    @ColumnWidth(value = 30)
    private String username;

    /**
     * 数据
     */
    @ExcelProperty(value = "data")
    @ColumnWidth(value = 30)
    private String data;
}