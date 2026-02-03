package com.nbtech.ailab.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MaterialTypeListVo {

    private String typeName;

    private List<TagVo> tagList;
}
