package com.nbtech.ailab.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 算子字数统计VO
 *
 * @author nber
 */
@Data
public class ElementWordNumberVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 算子id
     */
    private String elementId;

    /**
     * 字数总和
     */
    private Long wordNumber;
}
