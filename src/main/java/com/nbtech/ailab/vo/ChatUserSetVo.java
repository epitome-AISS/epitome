package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class ChatUserSetVo {
    private String roleType;
    private String roleName;
    private String roleDes;
    private String roleDesDetail;
    private String roleHead;
    private Boolean isHost;
    private Integer id;
    private Integer models;
    private String modelName;
    private String url;
    private String apiKey;
    private Integer contextLength;
}
