package com.nbtech.ailab.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nber
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PyParamVo {

    // 目标文本
    private String target;

    // minioPath
    private String minioPath;

    // 实验id
    private Long experimentId;

    // 实验组id
    private Long groupId;

    // 问卷id
    private Long questionnaireId;

    // 题目序号
    private String questionSort;

}