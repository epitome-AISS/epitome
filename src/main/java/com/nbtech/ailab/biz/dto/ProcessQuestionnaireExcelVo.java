package com.nbtech.ailab.biz.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ProcessQuestionnaireExcelVo {

//    @ApiModelProperty(value = "实验组id")
//    @ExcelProperty(value = "groups Id")
//    @ColumnWidth(value = 30)
//    private Long groupsId;



//    @ApiModelProperty(value = "算子Id")
//    @ExcelProperty(value = "element Id")
//    @ColumnWidth(value = 30)
//    private String elementId;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty(value = "user name")
    @ColumnWidth(value = 30)
    private String username;

    @ApiModelProperty(value = "回合数")
    @ExcelProperty(value = "round")
    @ColumnWidth(value = 30)
    private Integer round;

    @ApiModelProperty(value = "题目序号")
    @ExcelProperty(value = "question Sort")
    @ColumnWidth(value = 30)
    private Long questionSort;


    @ApiModelProperty(value = "题目名称")
    @ExcelProperty(value = "question Name")
    @ColumnWidth(value = 30)
    private String questionName;

    @ApiModelProperty(value = "答案")
    @ExcelProperty(value = "answer")
    @ColumnWidth(value = 30)
    private String answer;

//    @ApiModelProperty(value = "用户id")
//    @ExcelProperty(value = "user Id")
//    @ColumnWidth(value = 30)
//    private Long userId;



//    @ApiModelProperty(value = "流程id")
//    @ExcelProperty(value = "process Id")
//    @ColumnWidth(value = 30)
//    private String processId;
//
//    @ApiModelProperty(value = "问卷id")
//    @ExcelProperty(value = "questionnaire Id")
//    @ColumnWidth(value = 30)
//    private Long questionnaireId;


}
