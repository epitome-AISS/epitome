package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * @author nber
 */

@Getter
public enum PlanStatusEnum {

    /**
     * 待提交
     */
    BESUBMIT( "beSubmit"),

    /**
     * 待审核
     */
    BEAUDIT("beAudit"),

    /**
     * 待发布
     */
    BEPUBLISH ("bePublish"),

    /**
     * 待完成
     */
    BEEND("beEnd"),

    /**
     * 完成
     */
    END("end"),

    /**
     * 暂停
     */
    PAUSE("pause");


    private String desc;

    PlanStatusEnum(String desc) {
        this.desc = desc;

    }

}
