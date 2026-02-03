package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class AuthenticationVo {

    /**
     * 身份描述
     */
    private String description;

    /**
     * 身份属性
     */
    private String attribute;

    /**
     * 输入身份数据
     */
    private String input;
}
