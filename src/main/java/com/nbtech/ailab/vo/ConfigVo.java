package com.nbtech.ailab.vo;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class ConfigVo {

    public Integer dialogueId;

    public Long groupId;

    private String displayProcess;

    private Long minTurns;

}
