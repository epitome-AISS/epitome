package com.nbtech.ailab.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Data
@ApiModel(value = "")
public class SysUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String username;

    @ApiModelProperty(value = "")
    private String password;

    @ApiModelProperty(value = "")
    private String realName;

    @ApiModelProperty(value = "")
    private String email;

    @ApiModelProperty(value = "")
    private String mobile;

    @ApiModelProperty(value = "")
    private String deptName;

    @ApiModelProperty(value = "")
    private Integer superAdmin;

    @ApiModelProperty(value = "")
    private Integer status;

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

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "英文名")
    private String englishNames;

    @ApiModelProperty(value = "角色id")
    private String roleIds;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "dify邮箱账号")
    private String difyAccountMail;

    @ApiModelProperty(value = "dify账号")
    private String difyAccount;

    @ApiModelProperty(value = "dify密码")
    private String difyPassword;
    /**
     * 实验id集合
     */
    @ApiModelProperty(value = "实验id集合")
    private List<Long> experimentIds;
    /**
     * 实验组id集合
     */
    @ApiModelProperty(value = "实验组id集合")
    private List<Long> groupIds;

}