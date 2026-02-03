package com.nbtech.ailab.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class EdgeDataDto {

    private List<String> targetRoleName;
    private List<String> roleNameSelect;
}
