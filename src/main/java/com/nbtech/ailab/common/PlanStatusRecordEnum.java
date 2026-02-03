package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum PlanStatusRecordEnum {

    /**
     * 提交
     */
    SUBMIT( "submit"),

    /**
     * 审核
     */
    AUDIT("audit"),

    /**
     * 审核不通过
     */
    FAILSAUDIT("failsAudit"),

    /**
     * 发布
     */
    PUBLISH ("publish"),

    /**
     * 完成
     */
    END("end"),

    /**
     * 暂停
     */
    PAUSE("pause");

    private String desc;
    PlanStatusRecordEnum(String desc) {
        this.desc = desc;

    }
}
