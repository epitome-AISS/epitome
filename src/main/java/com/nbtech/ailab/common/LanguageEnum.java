package com.nbtech.ailab.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum LanguageEnum {

    ZH_CN("zh_CN", "中文/中国"),
    EN_US("en_US", "英语/美国"),
    ;

    /**
     * 语言_国家缩写
     */
    private final String name;

    /**
     * 描述
     */
    private final String desc;

    public static List<String> names() {
        List<String> names = new ArrayList<>();
        for (LanguageEnum value : LanguageEnum.values()) {
            names.add(value.getName());
        }
        return names;
    }

}
