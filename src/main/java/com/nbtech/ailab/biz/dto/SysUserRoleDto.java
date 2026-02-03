package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.time.LocalDateTime;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Data
@ApiModel(value = "")
public class SysUserRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "")
	private Integer userId;

	@ApiModelProperty(value = "")
	private Long roleId;

	@ApiModelProperty(value = "")
	private Integer isDeleted;

	@ApiModelProperty(value = "")
	private Long creator;

	@ApiModelProperty(value = "")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "")
	private Long updater;

	@ApiModelProperty(value = "")
	private LocalDateTime updateDate;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;




}