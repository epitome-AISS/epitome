package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class ModelInfoVo {

    private String name;

    private String chineseName;

    private String englishName;

    private String chineseDesc;

    private String englishDesc;

    private String useStatus;

    private Integer userId;

    private String basicModelAttribution;

    private String url;

    private String apiKey;

    private String contextLength;

    private String model_provider;

}
