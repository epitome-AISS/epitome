package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
@Data
@TableName("t_experiment_message")
public class ExperimentMessageEntity {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 实验组id
     */
    private Long groupId;
    /**
     * 进行中人数
     */
    private Long processingNumber;
    /**
     * 完成人数
     */
    private Long finishedNumber;
    /**
     * 累计完成人数
     */
    private Long sumNumber;
    /**
     * 实验统计当前日期
     */
    private LocalDate recordDate;
}