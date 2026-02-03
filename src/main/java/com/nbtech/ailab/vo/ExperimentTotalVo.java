package com.nbtech.ailab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel("实验统计vo")
public class ExperimentTotalVo {
    @ApiModelProperty(value = "进行中人数")
    private Long processingNumber;

    @ApiModelProperty(value = "完成人数")
    private Long finishedNumber;

    @ApiModelProperty(value = "累计完成人数")
    private Long sumNumber;

    @ApiModelProperty("实验统计当前日期")
    private LocalDate recordDate;
}
