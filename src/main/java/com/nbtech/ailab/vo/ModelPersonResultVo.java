package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nber
 */
@Data
public class ModelPersonResultVo {

    /**
     * 最大数
     */
    public int maxNum;

    /**
     * 最小数
     */
    public int minNum;

    /**
     * 模型结果集合
     */
    public List<ModelCountVo> modelCountVos;

    public ModelPersonResultVo(int minNum, int maxNum) {
        this.maxNum = maxNum;
        this.minNum = minNum;
    }
    public ModelPersonResultVo() {
    }
}
