package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class HomeRecordVo {

    /**
     * 实验数
     */
    private Integer experimentNumber;
    /**
     * 实验人数
     */
    private Integer personNumber;
    /**
     * 研究者人数
     */
    private Integer researcherNumber;
    /**
     * 开源工具数
     */
    private Integer openNumber;
}
