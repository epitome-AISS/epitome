package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@ApiModel(value = "实验人群包")
public class GroupsPersonDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "人群id")
    private Long id;

    @ApiModelProperty(value = "实验编号")
    private String experimentCode;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    /**
     * 地域
     */
    private String address;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "实验名称")
    private String experimentName;

    @ApiModelProperty(value = "实验组id")
    private Long groupsId;

    @ApiModelProperty(value = "实验组名称")
    private String groupsName;

    @ApiModelProperty(value = "实验完成状态")
    private String experimentStatus;

    @ApiModelProperty(value = "受试者ip")
    private String ip;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "删除 0否/1是")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "实验id")
    private Long experimentId;
}