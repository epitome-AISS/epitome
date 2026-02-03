package com.nbtech.ailab.common;

import lombok.Getter;

/**
 * 基础模型可用状态枚举
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@Getter
public enum BasicModelAvailableStatusEnum {
    /**
     * 可用
     */
    AVAILABLE,
    /**
     * 不可用
     */
    UNAVAILABLE;
}
