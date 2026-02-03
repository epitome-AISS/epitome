package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class UpdateWfParamVo {

    private String description;

    private String icon = "\uD83E\uDD16";

    private String icon_background = "#FFEAD5";

    private String icon_type ="emoji";

    private Boolean use_icon_as_answer_icon = false;

    private String name;
}
