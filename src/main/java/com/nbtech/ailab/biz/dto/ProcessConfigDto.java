package com.nbtech.ailab.biz.dto;

import lombok.Data;

/**
 * 流程配置 dto
 *  
 * @author  hsp
 */
@Data
public class ProcessConfigDto {


    /**
     * 算子id
     */
    private String id;

    /**
     * 算子类型
     */
    private String type;

    /**
     * 算子顺序
     */
    private Integer sequence;

    /**
     * 算子配置
     */
    private String config;
    
}
