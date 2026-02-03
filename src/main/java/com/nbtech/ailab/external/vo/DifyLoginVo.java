package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class DifyLoginVo {

    private String email;

    private String language = "zh-Hans";

    private String password;

    private Boolean remember_me = true;
}
