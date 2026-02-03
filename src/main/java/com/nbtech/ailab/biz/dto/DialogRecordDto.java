package com.nbtech.ailab.biz.dto;

import lombok.Data;

/**
 * 对话记录dto
 * 
 * @author hsp
 */
@Data
public class DialogRecordDto {

    /**
     * 类型 text / image_url
     */
    private String type;
    /**
     * 内容
     */
    private String text;
    /**
     * 图片url
     */
    private ImagerUrlDto image_url;    
}
