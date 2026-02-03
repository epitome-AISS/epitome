package com.nbtech.ailab.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 聊天室聊天记录excel字段
 */
@Data
@ApiModel(value = "聊天室聊天记录excel字段")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class RoomChatHistoryExcelVo {

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
    /**
     * 用户名
     */
    @ExcelProperty(value = "Account Name")
    @ApiModelProperty(value = "账号名")
    @ColumnWidth(value = 30)
    private String username;
    /**
     * 角色名称
     */
    @ExcelProperty(value = "Role Name")
    @ApiModelProperty(value = "角色名称")
    @ColumnWidth(value = 30)
    private String roleName;
    /**
     * 是否是主持人
     */
    @ExcelProperty(value = "Is it the host")
    @ApiModelProperty(value = "是否是主持人")
    @ColumnWidth(value = 30)
    private Boolean isHost;
    /**
     * 角色类型
     */
    @ExcelProperty(value = "Role type")
    @ApiModelProperty(value = "角色类型")
    @ColumnWidth(value = 30)
    private String roleType;
    /**
     * 发言顺序
     */
    @ExcelProperty(value = "Order of Speeches")
    @ApiModelProperty(value = "发言顺序")
    @ColumnWidth(value = 30)
    private Integer sort;
    /**
     * 发言记录时间
     */
    @ExcelProperty(value = "Speech time")
    @ApiModelProperty(value = "发言时间")
    @ColumnWidth(value = 30)
    private BigDecimal recordTime;
    /**
     * 发言记录
     */
    @ExcelProperty(value = "Speech content")
    @ApiModelProperty(value = "发言内容")
    @ColumnWidth(value = 50)
    private String record;

}
