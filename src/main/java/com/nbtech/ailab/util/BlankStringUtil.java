package com.nbtech.ailab.util;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author nber
 */
public class BlankStringUtil {

    /**
     * 校验为空
     * @param o
     * @return
     */
    public static boolean isBlank(Object o){
        return Optional.ofNullable(o).isPresent() && !o.toString().isEmpty();
    };
}
