package com.nbtech.ailab.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 实验计划完成情况统计VO
 *
 * @author nber
 */
@Data
@ApiModel("实验计划完成情况统计")
public class PlanCompletionVo {

    @ApiModelProperty(value = "实验计划完成百分比（保留两位小数）")
    private BigDecimal planCompletionPercent;

    @ApiModelProperty(value = "实验计划完成人数")
    private Long planFinishedNumber;

    @ApiModelProperty(value = "实验计划总人数")
    private Long planTotalNumber;

    @ApiModelProperty(value = "各实验组完成情况列表")
    private List<GroupCompletionVo> groupCompletionList;

    /**
     * 实验组完成情况VO
     */
    @Data
    @ApiModel("实验组完成情况")
    public static class GroupCompletionVo {
        @ApiModelProperty(value = "实验组ID")
        private Long groupId;

        @ApiModelProperty(value = "实验组名称")
        private String groupName;

        @ApiModelProperty(value = "实验组完成百分比（保留两位小数）")
        private BigDecimal groupCompletionPercent;

        @ApiModelProperty(value = "实验组完成人数")
        private Long groupFinishedNumber;

        @ApiModelProperty(value = "实验组总人数")
        private Long groupTotalNumber;
    }
}
