package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.dto.HistoryRecordDto;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author nber
 */
@Data
public class RecordParamVo implements Serializable {

    @ApiModelProperty(value = "实验组id")
    private Long groupsId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "算子id")
    private String elemenId;

    @ApiModelProperty(value = "基础模型id")
    private String modelName;

    @ApiModelProperty(value = "基础模型id")
    private Long modelId;

    @ApiModelProperty(value = "对话轮次")
    private Integer totalCount;

    @ApiModelProperty(value = "模型对话记录集合")
    private List<HistoryRecordDto> historyRecordList;
}
