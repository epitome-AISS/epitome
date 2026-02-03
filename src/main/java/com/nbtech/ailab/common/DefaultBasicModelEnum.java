package com.nbtech.ailab.common;

import com.nbtech.ailab.biz.entity.BasicModelEntity;
import com.nbtech.ailab.vo.DefaultModelVo;
import lombok.Getter;

@Getter
public enum DefaultBasicModelEnum {

    QWEN_INSTRUCT("qwen2-instruct", "qwen2-instruct", "EMPTY", "llm", "http://nas.maiff.cn:19997/");

    private DefaultModelVo model;

    DefaultBasicModelEnum(String modelId,
                          String modelName,
                          String key,
                          String modelType,
                          String url
    ) {
        model = new DefaultModelVo();
        model.setModelId(modelId);
        model.setModelName(modelName);
        model.setModelType(modelType);
        model.setKey(key);
        model.setUrl(url);

    }

}
