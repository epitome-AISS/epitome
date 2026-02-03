package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class QuestionStarDataAnswerVo {

    private List<List<String>> AnswerHeadList;

    private List<List<Object>> AnswerDataList;
}
