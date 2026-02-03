package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class RecordStatusVo {

    /**
     * 提交
     */
    private String submit;

    /**
     * 审核
     */
    private String audit;

    /**
     * 审核不通过
     */
    private String failsAudit;

    /**
     * 发布
     */
    private String publish;

    /**
     * 完成
     */
    private String end;

    /**
     * 暂停
     */
    private String pause;

    /**
     * 实验计划id
     */
    private String planId;
}
