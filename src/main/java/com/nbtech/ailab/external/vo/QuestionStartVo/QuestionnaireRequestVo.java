package com.nbtech.ailab.external.vo.QuestionStartVo;

import lombok.Data;

@Data
public class QuestionnaireRequestVo {

    private String activityId;       // 问卷编号
    private String activityName;     // 问卷标题
    private String activityDomain;  // 问卷访问域名
    private String activityPCUrl;    // PC端链接
    private String activityH5Url;   // 移动端链接
    private String wjxparams;

    private String content;         // AES 加密的问卷
}
