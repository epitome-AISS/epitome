package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nber
 */
@Data
public class ModelJsonVo {

    String dialogueId;

    Long questionnaireId;
    /**
     * 是否展示思考过程
     */
    Boolean displayProcess;
    /**
     * 模型组id
     */
    Long groupId;

    /**
     * 最小对话轮次
     */
    Long minTurns;

    String canCopy;

}
