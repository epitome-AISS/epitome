package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class PushRequestVo {
    /**
     * 元素ID
     */
    private String element_id;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 请求数据
     */
    private PushData data;

    @Data
    public static class PushData {
        /**
         * 步骤
         */
        private Integer step;

        /**
         * 选择
         */
        private String choice;
    }
}