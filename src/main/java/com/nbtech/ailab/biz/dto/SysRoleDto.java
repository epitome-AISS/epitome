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
public class SysRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "")
	private Integer parentId;

	@ApiModelProperty(value = "")
	private String name;

    @ApiModelProperty(value = "角色英文名")
    private String englishName;

	@ApiModelProperty(value = "")
	private Integer type;

	@ApiModelProperty(value = "")
	private String title;

	@ApiModelProperty(value = "")
	private String remark;

	@ApiModelProperty(value = "")
	private String halfCheckedKeys;

    /**
     * 数据权限标识 1 超级管理
     */
    @ApiModelProperty(value = "数据权限标识 1 超级管理")
    private Integer tag;

	@ApiModelProperty(value = "")
	private String messageHalfCheckedKeys;

	@ApiModelProperty(value = "")
	private String permissions;

	@ApiModelProperty(value = "")
	private String messagePermissions;

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