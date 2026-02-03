package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * 问卷算子id
 * @author nber
 */
@Data
public class QuestionElementVo {

    /**
     * 算子id
     */
    private String elementId;

    /**
     * 问卷id
     */
    private Long questionnaireId;

    /**
     * 序号
     */
    private Integer sequence;
}
