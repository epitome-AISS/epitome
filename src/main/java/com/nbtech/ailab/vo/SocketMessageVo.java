package com.nbtech.ailab.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class SocketMessageVo {
    private String type;
    private Object state;
}
