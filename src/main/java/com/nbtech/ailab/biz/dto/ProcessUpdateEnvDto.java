package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 环境变量修改记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-06
 */
@Data
@ApiModel(value = "环境变量修改记录")
public class ProcessUpdateEnvDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "实验组id")
	private Long groupId;

	@ApiModelProperty(value = "第几回合")
	private Integer round;

	@ApiModelProperty(value = "算子id")
	private String elementId;

	@ApiModelProperty(value = "环境变量名称")
	private String envName;

	@ApiModelProperty(value = "环境变量描述")
	private String envDes;

	@ApiModelProperty(value = "")
	private String envScope;

	@ApiModelProperty(value = "所属角色的环境变量")
	private String roleName;

	@ApiModelProperty(value = "更新方式")
	private String updateMethod;

	@ApiModelProperty(value = "更新后的环境变量配置")
	private String envConfig;

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