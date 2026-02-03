package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OperationRecordVo {

    /**
     * 操作方式
     */
    private String operation;

    /**
     * 操作备注
     */
    private String explaination;

    /**
     * 操作时间
     */
    private Date createDate;
}
