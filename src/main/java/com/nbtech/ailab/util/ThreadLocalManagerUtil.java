package com.nbtech.ailab.util;

import lombok.Data;
import com.nbtech.ailab.common.LanguageEnum;

public class ThreadLocalManagerUtil {
    @Data
    public static class HeaderInfo {

        /**
         * 国际化语言包名称
         */
        private String language;
    }

    /**
     * 存储请求头信息
     */
    private final static ThreadLocal<HeaderInfo> headerInfoThreadLocal = new ThreadLocal<>();

    public static void add(HeaderInfo headerInfo) {
        headerInfoThreadLocal.set(headerInfo);
    }

    public static String getLanguage() {
        if (headerInfoThreadLocal.get() == null) {
            return LanguageEnum.ZH_CN.getName();
        }
        if (headerInfoThreadLocal.get().getLanguage() == null) {
            return LanguageEnum.ZH_CN.getName();
        }
        return headerInfoThreadLocal.get().getLanguage();
    }

    /**
     * 释放资源
     */
    public static void remove() {
        headerInfoThreadLocal.remove();
    }
}
