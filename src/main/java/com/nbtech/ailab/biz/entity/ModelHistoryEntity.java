package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模型问答历史
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Data
@TableName("t_model_history")
public class ModelHistoryEntity {

    /**
     * 历史id
     */
	private Long id;
    /**
     * 实验组id
     */
	private Long groupsId;
    /**
     * 用户id
     */
	private Long userId;
    /**
     * 算子id
     */
	private String elemenId;

    private Integer totalCount;
    /**
     * 模型名称
     */
	private String modelName;

    /**
     * 基础模型id
     */
    private Long modelId;

    /**
     * 
     */
	private Long creator;
    /**
     * 制单时间
     */
	private LocalDateTime createDate;
    /**
     * 
     */
	private Long updater;
    /**
     * 更新时间
     */
	private LocalDateTime updateDate;
    /**
     * 删除 0否/1是
     */
    @TableLogic
	private Integer isDeleted;
    /**
     * 更新人名称
     */
	private String updateName;
    /**
     * 创建人名称
     */
	private String createName;
}