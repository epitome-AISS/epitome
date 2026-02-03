package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 环境变量修改记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-06
 */
@Data
@TableName("t_process_update_env")
public class ProcessUpdateEnvEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 实验组id
     */
	private Long groupId;
    /**
     * 第几回合
     */
	private Integer round;
    /**
     * 算子id
     */
	private String elementId;
    /**
     * 环境变量名称
     */
	private String envName;
    /**
     * 环境变量描述
     */
	private String envDes;
    /**
     * 
     */
	private String envScope;
    /**
     * 所属角色的环境变量
     */
	private String roleName;
    /**
     * 更新方式
     */
	private String updateMethod;
    /**
     * 更新后的环境变量配置
     */
	private String envConfig;
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