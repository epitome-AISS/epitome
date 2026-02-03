package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 大模型问答(填空、简答)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
public class ModelRecordEntity {

    /**
     * 模型问答id
     */
    private Long id;
    /**
     * 实验组id
     */
    private Long groupsId;
    /**
     * 算子顺序编号
     */
    private Integer operatorPrecedence;
    /**
     * 大模型名称
     */
    private String modelName;
    /**
     * 问答回合数
     */
    private Integer roundCount;
    /**
     * 问题总字数
     */
    private Integer questionWords;
    /**
     * 回答总字数
     */
    private Integer answerWords;
    /**
     * 回答总耗时
     */
    private Integer spentTime;
    /**
     * 用户数量
     */
    private Integer userNumber;
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

    @TableField(exist = false)
    private String role;
}