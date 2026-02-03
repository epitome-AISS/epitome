package com.nbtech.ailab.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nber
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NextElementVo {

    /**
     * 实验计划id
     */
    private Long experimentId;

    /**
     * 实验组id
     */
    private Long groupsId;

    /**
     * 算子id
     */
    private String elementId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 算子序号
     */
    private Integer sequence;

}
