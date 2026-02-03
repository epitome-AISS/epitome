package com.nbtech.ailab.biz.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 流程任务记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-12-24
 */
@Data
@ApiModel(value = "流程任务记录")
public class ProcessWorkRecordDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "执行人id")
	private Long userId;

	@ApiModelProperty(value = "执行人的角色")
	private String roleName;

	@ApiModelProperty(value = "执行人类型")
	private String userType;

	@ApiModelProperty(value = "执行的第几个回合")
	private Integer round;

	@ApiModelProperty(value = "实验组id")
	private Long groupId;

	@ApiModelProperty(value = "算子id")
	private String elementId;

	@ApiModelProperty(value = "聊天室id")
	private Long roomId;

	@ApiModelProperty(value = "流程id")
	private String processId;

	@ApiModelProperty(value = "任务id")
	private String workId;

	@ApiModelProperty(value = "数据id")
	private String flowId;

	@ApiModelProperty(value = "任务类型")
	private String workType;

	private String flowType;

	private Integer isPreview;

	private Integer isDeleted;
	/**
	 *
	 */
	private Long creator;
	/**
	 *
	 */
	private LocalDateTime createDate;
	/**
	 *
	 */
	private Long updater;
	/**
	 *
	 */
	private LocalDateTime updateDate;
	/**
	 * 更新人姓名
	 */
	private String updateName;
	/**
	 * 创建人姓名
	 */
	private String createName;


	/**
	 * 聊天室结构表id
	 */
	private Long structureId;


}