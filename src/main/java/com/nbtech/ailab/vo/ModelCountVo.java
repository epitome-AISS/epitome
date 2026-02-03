package com.nbtech.ailab.vo;

import lombok.Data;

/**
 * @author nber
 */
@Data
public class ModelCountVo {

    /**
     * 模型名称
     */
    public String modelName;

    /**
     * 基础模型id
     */
    public Long modelId;

    /**
     * 统计次数
     */
    public Integer countNum;

}
