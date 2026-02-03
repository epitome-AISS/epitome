package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Data
@TableName("t_groups_person")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupsPersonEntity {

    /**
     * 人群id
     */
    private Long id;
    /**
     * 实验编号
     */
    private String experimentCode;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDate;
    /**
     * 实验名称
     */
    private String experimentName;
    /**
     * 实验组id
     */
    private Long groupsId;
    /**
     * 受试者ip
     */
    private String ip;
    /**
     * 实验组名称
     */
    private String groupsName;
    /**
     * 地域
     */
    private String address;
    /**
     * 实验完成状态
     */
    private String experimentStatus;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 删除 0否/1是
     */
    @TableLogic
    private Integer isDeleted;
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
    /**
     * 密码
     */
    private String password;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 实验id
     */
    private Long experimentId;
}