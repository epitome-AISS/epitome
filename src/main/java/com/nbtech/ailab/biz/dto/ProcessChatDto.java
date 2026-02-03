package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 流程聊天室输入
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-01-15
 */
@Data
@ApiModel(value = "流程聊天室输入")
public class ProcessChatDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "实验组id")
    private Long groupId;

    @ApiModelProperty(value = "算子id")
    private String elementId;

    @ApiModelProperty(value = "算子序号")
    private Integer elementSort;

    @ApiModelProperty(value = "聊天室id")
    private Long roomId;

    @ApiModelProperty(value = "流程id")
    private String processId;

    @ApiModelProperty(value = "任务id")
    private String workId;

    @ApiModelProperty(value = "任务类型")
    private String workType;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "第几回合")
    private Integer round;

    @ApiModelProperty(value = "环境变量名称")
    private String envName;

    @ApiModelProperty(value = "聊天信息")
    private String chatMessage;

    @ApiModelProperty(value = "是否已经展示")
    private Boolean displayStatus;

    @ApiModelProperty(value = "是否是预览")
    private Integer isPreview;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "删除 0否/1是")
    private Integer isDeleted;


}