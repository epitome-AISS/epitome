package com.nbtech.ailab.biz.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * @author nber
 */
@Data
@ApiModel("数据包下问卷算子dto")
@ContentRowHeight(20)
@HeadRowHeight(value = 20)
public class QuestionnaireInfoDto implements Comparable<QuestionnaireInfoDto> {

    @ApiModelProperty(value = "用户id")
    @ExcelIgnore
    private Long userId;

    @ApiModelProperty(value = "编号 问卷id")
    @ExcelProperty(value = "numbering")
    @ColumnWidth(value = 10)
    private Long questionnaireId;

    @ApiModelProperty(value = "算子id")
    @ExcelIgnore
    private String elementId;

    @ApiModelProperty(value = "算子名称")
    @ExcelIgnore
    private String elementName;

    @ApiModelProperty(value = "问题类型")
    @ExcelIgnore
    private int type;

    @ApiModelProperty(value = "题目序号")
    private Integer questionSort;
    /**
     * 问题类型转换为可理解字段
     */
    @ExcelProperty(value = "type")
    @ColumnWidth(value = 20)
    private String typeInterpretation;

    @ApiModelProperty(value = "题目")
    @ExcelProperty(value = "questionName")
    @ColumnWidth(value = 30)
    private String questionName;

    @ApiModelProperty(value = "回答")
    @ExcelProperty(value = "answer")
    @ColumnWidth(value = 30)
    private String qaData;

    @ApiModelProperty(value = "开始时间")
//    @ExcelProperty(value = "startTime")
//    @ColumnWidth(value = 30)
    @ExcelIgnore
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
//    @ExcelProperty(value = "endTime")
//    @ColumnWidth(value = 30)
    @ExcelIgnore
    private LocalDateTime endTime;

    @ApiModelProperty(value = "耗时(秒)")
    @ExcelProperty(value = "Time(s)")
    @ColumnWidth(value = 20)
    private int second;



    @Override
    public int compareTo(@NotNull QuestionnaireInfoDto o) {
        return Long.compare(this.questionSort, o.getQuestionSort());
    }
}
