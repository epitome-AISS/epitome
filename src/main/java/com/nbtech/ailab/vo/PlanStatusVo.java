package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class PlanStatusVo {


    /**
     * 新建
     */
    private String beSubmit;
    /**
     * 待审核
     */
    private String beAudit;

    /**
     * 待发布
     */
    private String bePublish;
    /**
     * 进行中
     */
    private String beEnd;

    /**
     * 已完成
     */
    private String end;

    /**
     * 新建个数
     */
    private Integer beSubmitNum;
    /**
     * 待审核个数
     */
    private Integer beAuditNum;

    /**
     * 待发布个数
     */
    private Integer bePublishNum;
    /**
     * 进行中个数
     */
    private Integer beEndNum;

    /**
     * 已完成个数
     */
    private Integer endNum;


}
