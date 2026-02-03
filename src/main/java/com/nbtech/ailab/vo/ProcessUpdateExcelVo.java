package com.nbtech.ailab.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessUpdateExcelVo {


    /**
     * 第几回合
     */
    @ExcelProperty(value = "round")
    @ApiModelProperty(value = "第几回合")
    @ColumnWidth(value = 30)
    private Integer round;
    /**
     * 算子id
     */
    @ExcelProperty(value = "element id")
    @ApiModelProperty(value = "算子id")
    @ColumnWidth(value = 30)
    private String elementId;
    /**
     * 环境变量名称
     */
    @ExcelProperty(value = "env name")
    @ApiModelProperty(value = "环境变量名称")
    @ColumnWidth(value = 30)
    private String envName;
    /**
     * 环境变量描述
     */
    @ExcelProperty(value = "env des")
    @ApiModelProperty(value = "环境变量描述")
    @ColumnWidth(value = 30)
    private String envDes;
    /**
     *
     */
    @ExcelProperty(value = "env scope")
    @ApiModelProperty(value = "环境变量类型")
    @ColumnWidth(value = 30)
    private String envScope;
    /**
     * 所属角色的环境变量
     */
    @ExcelProperty(value = "role name")
    @ApiModelProperty(value = "所属角色的环境变量")
    @ColumnWidth(value = 30)
    private String roleName;
    /**
     * 更新方式
     */
    @ExcelProperty(value = "update method")
    @ApiModelProperty(value = "更新方式")
    @ColumnWidth(value = 30)
    private String updateMethod;
    /**
     * 更新后的环境变量配置
     */
    @ExcelProperty(value = "env config")
    @ApiModelProperty(value = "更新后的环境变量配置")
    @ColumnWidth(value = 30)
    private String envConfig;

}
