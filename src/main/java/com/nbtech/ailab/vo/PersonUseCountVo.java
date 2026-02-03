package com.nbtech.ailab.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nber
 */
@Data
@AllArgsConstructor
public class PersonUseCountVo {

    /**
     * 用户id
     */
    public Long userId;

    /**
     * 模型名称
     */
    public String modelName;

    /**
     * 基础模型id
     */
    public Long modelId;

    /**
     * 对话次数
     */
    public Integer number;


}
