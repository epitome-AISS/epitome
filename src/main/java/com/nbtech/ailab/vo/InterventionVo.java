package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nber
 */
@Data
public class InterventionVo {


    /**
     * 展示方式
     */
    public String displayType;

    /**
     * 干预对象
     */
    public InterveneVo material;

    private String canCopy;

    
}
