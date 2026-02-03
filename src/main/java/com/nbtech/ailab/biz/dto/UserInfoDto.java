package com.nbtech.ailab.biz.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author nber
 */
@Data
@ApiModel("用户信息")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class UserInfoDto {

    @ApiModelProperty(value = "用户id")
    @ExcelIgnore
    private Long userId;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty(value = "userName")
    @ColumnWidth(value = 20)
    private String userName;

    @ApiModelProperty(value = "IP地址")
    @ExcelProperty(value = "ip")
    @ColumnWidth(value = 20)
    private String ip;

    @ApiModelProperty(value = "地域")
    @ExcelProperty(value = "address")
    @ColumnWidth(value = 20)
    private String address;

    @ApiModelProperty(value = "开始实验时间")
    @ExcelProperty(value = "startTime")
    @ColumnWidth(value = 30)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "完成实验时间")
    @ExcelProperty(value = "endTime")
    @ColumnWidth(value = 30)
    private LocalDateTime endTime;
}
