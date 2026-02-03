package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum QuStartEnum {


    /**
     * 问卷星登录/用户创建接口
     */
    CREATE_LOGIN("/getjoinlist.aspx"),

    /**
     * 获取问卷列表
     */
    GET_QN_LIST("/getuserq.aspx"),

    /**
     * 获取答卷数据
     */
    GET_QN_DATA("/getjoinlist.aspx"),

    /**
     * 查询参与者答卷数据
     */
    GET_USER_QN_LIST("/qlist.aspx");



    private final String url;

    QuStartEnum(String url) {
        this.url = url;
    }

}
