package com.nbtech.ailab.vo;

import lombok.Data;

@Data
public class ResponseData {

    private int code;
    private String message;
    private Object data;
}
