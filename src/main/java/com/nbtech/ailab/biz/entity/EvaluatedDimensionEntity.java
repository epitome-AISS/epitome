package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 受测用户维度
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@TableName("t_evaluated_dimension")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluatedDimensionEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 实验组id
     */
	private Long groupsId;
    /**
     * 地域
     */
	private String region;
    /**
     * 人数
     */
	private Integer personnelCount;
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