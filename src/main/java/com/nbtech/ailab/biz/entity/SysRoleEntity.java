package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Data
@TableName("sys_role")
public class SysRoleEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private Integer parentId;
    /**
     * 
     */
	private String name;

    /**
     * 角色英文名
     */
    private String englishName;
    /**
     * 
     */
	private Integer type;
    /**
     * 
     */
	private String title;
    /**
     * 
     */
	private String remark;

    /**
     * 数据权限标识 1 超级管理
     */
    private Integer tag;
    /**
     * 
     */
	private String halfCheckedKeys;
    /**
     * 
     */
	private String messageHalfCheckedKeys;
    /**
     * 
     */
	private String permissions;
    /**
     * 
     */
	private String messagePermissions;
    /**
     * 
     */
    @TableLogic
	private Integer isDeleted;
    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
	private Long creator;
    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;
    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateDate;
    /**
     * 更新人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateName;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;
}