package com.nbtech.ailab.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author nber
 */
@Data
public class ModelConfigVo {

    /**
     *
     */
    public Long id;

    /**
     *
     */
    public String chineseName;

    /**
     *
     */
    public String englishName;

    /**
     *
     */
    public String chineseDesc;

    /**
     *
     */
    public String englishDesc;
    /**
     * 模型名称
     */
    public String name;

//    /**
//     * 概率
//     */
//    public Double probability;

}
