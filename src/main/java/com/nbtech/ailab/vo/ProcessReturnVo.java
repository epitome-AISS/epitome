package com.nbtech.ailab.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class ProcessReturnVo {

    // 锁住的状态
    private Boolean lockStatus = true;

    // 返回的数据
    private Object config;

    // 返回的数据类型
    private String workType;

    // 是否需要继续进行下个任务的查询
    private Boolean canRun;



    private String elementId;

    private Long groupId;

    private String roleName;

    private Long roomId;

    private Long userId;

    private String userType;

    private Integer round;

    private String processId;

    private String workId;

    private Boolean newRound = false;

    private List<EnvTemplateVo> progressEnv;
}
