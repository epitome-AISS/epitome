package com.nbtech.ailab.external.vo;

import lombok.Data;

@Data
public class MadeWorkflowVo {

    private String description;

    private String icon = "\uD83E\uDD16";

    private String icon_background = "#FFEAD5";

    private String icon_type ="emoji";

    private String mode = "workflow";

    private String name;
}
