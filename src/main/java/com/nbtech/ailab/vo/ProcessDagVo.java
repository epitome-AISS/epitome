package com.nbtech.ailab.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDagVo {

    public List<NodeVo> nodes;

    public List<EdgeVo> edges;


}
