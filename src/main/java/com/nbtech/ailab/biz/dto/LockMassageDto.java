package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 锁住记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-12-27
 */
@Data
@ApiModel(value = "锁住记录")
public class LockMassageDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupId;

	@ApiModelProperty(value = "算子id")
	private String elementId;

	@ApiModelProperty(value = "流程id")
	private String processId;

	@ApiModelProperty(value = "聊天室id")
	private Long roomId;

	@ApiModelProperty(value = "第几轮次")
	private Integer round;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "角色名称")
	private String roleName;

	@ApiModelProperty(value = "任务id")
	private String workId;

	@ApiModelProperty(value = "是否是预览")
	private Integer isPreview;

	@ApiModelProperty(value = "是否打开状态(是 lock 否 unlock）")
	private Boolean status;

	@ApiModelProperty(value = "锁住的原因")
	private String lockReason;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "制单时间")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "")
	private Long updater;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateDate;

	@ApiModelProperty(value = "删除 0否/1是")
	private Integer isDeleted;

	@ApiModelProperty(value = "更新人名称")
	private String updateName;

	@ApiModelProperty(value = "创建人名称")
	private String createName;


}