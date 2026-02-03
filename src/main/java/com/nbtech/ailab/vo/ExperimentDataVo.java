package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.ProcessUpdateEnvEntity;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author nber
 */
@Data
@ApiModel("实验组数据vo")
public class ExperimentDataVo {

    // 用户信息
    private List<UserInfoDto> userInfos;

    // 用户维度的问卷算子集合
    private List<List<QuestionnaireInfoDto>> questionnaireInfos;

    // 用户维度的模型对话算子集合
    private List<List<ModelInfoDto>> modelInfos;

    // 表头信息
    private List<Map<Long, List<Long>>> headInfos;

    // 聊天室聊天记录
    private List<RoomChatHistoryExcelVo> chatHistoryExcelVos;

    // 每个算子的环境变量的更新记录
    private Map<String, List<ProcessUpdateEnvEntity>> processUpdateEnv;

    // 问卷星的答卷结果
    private List<List<QuestionStarDataEntity>> questionStarDataEntityList;

    // 流程问卷的答题结果
    private Map<ProcessQuestionnaireVo,List<ProcessQuestionnaireExcelVo>> questionnaireExcelVoMap;

    // 合作测评数据
    private List<PushDataVo> pushDataVoList;

    // 素材包运行实验结果
    private List<MaterialGroupListDto> materialGroupListDtoList;


}
