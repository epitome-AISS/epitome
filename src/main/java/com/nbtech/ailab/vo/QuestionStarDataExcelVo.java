package com.nbtech.ailab.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel(value = "问卷星答题结果excel数据")
public class QuestionStarDataExcelVo {
    /**
     * 问卷id
     */
    private String activity;
    /**
     * 问卷名称
     */
    private String name;
    /**
     * 答题人ip地址
     */
    private String ipaddress;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 题目相关信息
     */
    private String questionTitle;

    /**
     * 题目答题结果
     */
    private String questionData;

    /**
     * 所用时间
     */
    private String timetaken;
    /**
     * 提交时间
     */
    private String submittime;





}
