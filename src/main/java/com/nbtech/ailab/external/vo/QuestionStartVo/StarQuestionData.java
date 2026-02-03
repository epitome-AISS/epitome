package com.nbtech.ailab.external.vo.QuestionStartVo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.Data;

@Data
public class StarQuestionData {

    // 固定字段
    private String activity;
    private String name;
    private String ipaddress;
    private String province;
    private String city;
    private String index;
    private String joinid;
    private String timetaken;
    private String submittime;
    private String sign;
    private String totalvalue;
    private String sojumpparm;

}
