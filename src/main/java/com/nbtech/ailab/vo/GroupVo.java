package com.nbtech.ailab.vo;


import com.nbtech.ailab.biz.entity.GroupsEntity;
import lombok.Data;

@Data
public class GroupVo extends GroupsEntity {

    /**
     * 包含的干预组
     */
    private String intervention;

    /**
     * 包含的模型组
     */
    private String model;

    /**
     * 包含的数据收集组
     */
    private String dataCollection;
}
