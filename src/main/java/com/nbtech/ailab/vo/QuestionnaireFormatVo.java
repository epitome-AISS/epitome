package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.entity.QuestionnaireRecordEntity;
import com.nbtech.ailab.biz.entity.QuestionnaireScaleEntity;
import com.nbtech.ailab.biz.entity.QuestionnaireSelectionEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionnaireFormatVo {

    List<QuestionnaireScaleEntity> questionnaireScaleEntities;

    List<QuestionnaireSelectionEntity> questionnaireSelectionEntities;

    List<QuestionnaireRecordEntity> questionnaireRecordEntities;

}
