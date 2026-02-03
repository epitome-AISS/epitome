package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nber
 */
@Data
public class EleParamVo {

    // 算子id
    public String elementId;

    // 模型id
    public Long modelId;

    // 实验组id
    public Long groupId;

    // 基础模型名称集合
    public List<String> modelNames;

    // 基础模型id集合
    public List<Long> modelIds;
}
