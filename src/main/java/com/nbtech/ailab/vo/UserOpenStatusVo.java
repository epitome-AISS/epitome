package com.nbtech.ailab.vo;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class UserOpenStatusVo {
    /**
     * 算子id
     */
    private String elementId;

    /**
     * 聊天室使用状态
     */
    private String keepStatus;

    /**
     * 用户id
     */
    private Long userId;
}
