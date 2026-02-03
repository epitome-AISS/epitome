package com.nbtech.ailab.biz.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 问卷(选择题)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Data
@TableName("t_questionnaire_selection")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireSelectionEntity {

    /**
     * 选择题id
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
     * 题目序号
     */
    private Integer questionSort;
    /**
     * 问卷id
     */
	private Long questionnaireId;

    /**
     * 回合数
     */
    private Long round;

    /**
     * 数据来源  多人多轮 WORKPROCESS 普通问卷 null
     */
    private String sourceType;

    /**
     * 选择类型 0单选 1多选
     */
    private Integer selectType;
    /**
     * 算子id
     */
    private String elementId;
    /**
     * 题目编号
     */
	private String questionName;
    /**
     * 工作流id
     */
    private String processId;
    /**
     * 选项
     */
	private String choiceContext;
    /**
     * 做题耗时
     */
	private Integer spentTime;
    /**
     * 用户id
     */
	private Long userId;
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
    /**
     * 选项序号
     */
	private Integer choiceSequence;
}