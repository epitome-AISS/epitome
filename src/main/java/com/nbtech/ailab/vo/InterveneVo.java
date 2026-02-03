package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class InterveneVo {

    /**
     * 干预类型
     */
    public String type;

    /**
     * 干预名称
     */
    public String name;

    /**
     * 干预内容
     */
    public String content;

    /**
     * 干预组的id
     */
    public Long groupId;

    public String displayType;

    /**
     * 素材id
     */
    public Long materialId;


    private String canCopy;
}
