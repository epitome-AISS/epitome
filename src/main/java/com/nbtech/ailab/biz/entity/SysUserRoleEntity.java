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
@TableName("sys_user_role")
public class SysUserRoleEntity {

    /**
     * 
     */
	private Long id;
    /**
     * 
     */
	private Integer userId;
    /**
     * 
     */
	private Long roleId;
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