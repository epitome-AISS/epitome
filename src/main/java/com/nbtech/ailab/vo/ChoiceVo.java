package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class ChoiceVo {
    // 选项顺序
    public Integer choiceSort;

    // 选项内容
    public String choiceContext;

    // 验证内容
    public String verificationContext;

    public Long id;
    /**
     * 是否可修改
     */
    public String isChange;
}
