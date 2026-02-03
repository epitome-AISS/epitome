package com.nbtech.ailab.external.vo;

import lombok.Data;

import java.util.List;

@Data
public class ScoreRequestVo {
    private String element_id;
    private Long user_id;
    private ScoreData data;

    // 内部类表示data对象
    @Data
    public static class ScoreData {
        private List<ScoreItem> score;
    }

    // 内部类表示score数组中的对象
    @Data
    public static class ScoreItem {
        private Integer id;
        private Integer score;
        private String capability;
    }

}