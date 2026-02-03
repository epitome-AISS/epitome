package com.nbtech.ailab.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author nber
 */
@Data
@Accessors(chain = true)
public class ElementVo implements Comparable<ElementVo> {

    // 算子类型
    public String type;

    public Object config;

    public String id;

    public Long sequence;

    public Long minTurns;

    public Boolean displayProcess;

    private Long userId;

    @Override
    public int compareTo(ElementVo o) {
        return Long.compare(this.sequence, o.getSequence());
    }
}
