package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class ElementParamVo {

    /**
     * 实验组id
     */
    private Long groupId;

    /**
     * 算子id
     */
    private String element;

    /**
     * 用户Id
     */
    private Long userId;
}
