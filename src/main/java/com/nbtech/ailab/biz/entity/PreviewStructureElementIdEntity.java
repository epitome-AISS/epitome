package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预览多人多轮工作流的预算子id
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-29
 */
@Data
@TableName("t_preview_structure_element_id")
public class PreviewStructureElementIdEntity {

    /**
     * 主键
     */
	private Long id;
    /**
     * 多人多轮结构表id
     */
	private Long structureId;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 算子id
     */
	private String elementId;
    /**
     * 创建人
     */
	private Long creator;
    /**
     * 创建时间
     */
	private LocalDateTime createDate;
    /**
     * 更新人
     */
	private Long updater;
    /**
     * 更新时间
     */
	private LocalDateTime updateDate;
    /**
     * 创建人名称
     */
	private String createName;
    /**
     * 更新人名称
     */
	private String updateName;
    /**
     * 删除 0否/1是
     */
	private Integer isDeleted;
}