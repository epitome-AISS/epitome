package com.nbtech.ailab.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MessageVo {
    private Object record;
    private BigDecimal recordTime;
    private String roleName;
    private String roleType;
    private int wordNumber;
}
