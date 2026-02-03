package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class NoSocketMessageVo {
    private String type;
    private List<MessageVo> msg;
}
