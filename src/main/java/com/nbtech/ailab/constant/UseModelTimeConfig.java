package com.nbtech.ailab.constant;



import java.math.BigDecimal;

/**
 * 规定的默认模型问答记录时间
 * @author nber
 */
public interface UseModelTimeConfig {

    /**
     * 字数与时间的倍率
     */
    BigDecimal RATE = new BigDecimal("1.05");

    /**
     * 默认的最大差值倍数 计算与实际差值倍数
     */
    BigDecimal MULTIPLE = new BigDecimal("40");
}
