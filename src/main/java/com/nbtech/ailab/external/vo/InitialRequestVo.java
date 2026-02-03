package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class InitialRequestVo {
    /**
     * 元素ID
     */
    private String element_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 身份定义 user/agent
     */
    private String identity;

    /**
     * 请求数据
     */
    private InitialData data;

    @Data
    public static class InitialData {
        /**
         * 名称
         */
        private String name;

        /**
         * 年级
         */
        private String grade;

        /**
         * 模式
         */
        private String mode;
    }
}
