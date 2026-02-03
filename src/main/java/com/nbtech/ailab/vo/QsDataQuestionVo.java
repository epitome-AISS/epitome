package com.nbtech.ailab.vo;

import lombok.Data;

import java.util.List;

@Data
public class QsDataQuestionVo {

    private String question;

    private String questionText;

    private List<QsDataChooseVo> questionChoices;
}
