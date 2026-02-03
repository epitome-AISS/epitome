package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class ExperimentGroupVo {

    /**
     * 实验名称
     */
    private String experimentName;

    /**
     * 实验组名称
     */
    private String groupsName;

    /**
     * 流程配置
     */
    private String processConfig;
}
