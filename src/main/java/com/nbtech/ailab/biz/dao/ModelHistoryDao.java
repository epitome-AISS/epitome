package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ModelInfoDto;
import com.nbtech.ailab.biz.entity.HistoryRecordEntity;
import com.nbtech.ailab.biz.entity.ModelHistoryEntity;
import com.nbtech.ailab.biz.entity.ModelRecordEntity;
import com.nbtech.ailab.vo.EleParamVo;
import com.nbtech.ailab.vo.ElementParamVo;
import com.nbtech.ailab.vo.ModelUserVo;
import com.nbtech.ailab.vo.PersonUseCountVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模型问答历史
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Mapper
public interface ModelHistoryDao extends BaseMapper<ModelHistoryEntity> {


    /**
     * 获取model 某个实验 某个用户 某几个模型的对话记录
     */
    List<HistoryRecordEntity> getModelRecord(Long groupId, String elementId, Long userId, List<String> modelNames);

    /**
     * elementParamVo 实验组ID 算子ID
     *
     * @return
     */
    String getLastModel(ElementParamVo elementParamVo);

    /**
     * 获取每个实验组下的当前算子模型组的问答回合数和问答总字数
     *
     * @param groupId    实验组id
     * @param modelNames 模型名称集合
     * @param user       用户角色
     * @return
     */
    List<ModelRecordEntity> getTotalRound(Long groupId, List<String> modelNames, String user);

    /**
     * 统计整个实验组实验模型下的 回答时间和回答总字数
     *
     * @param record 模型记录
     * @return
     */
    ModelRecordEntity getTotalAnswers(ModelRecordEntity record);

    /**
     * 统计整个实验组实验模型下的
     *
     * @param record 模型记录
     * @return
     */
    Integer getTotalUser(ModelRecordEntity record);

    /**
     * 根据模型算子获取各个模型的使用人数
     */
    List<ModelUserVo> getModelUserNumber(EleParamVo eleParamVo);

    /**
     * 统计每个人跟每个模型对话的总次数
     */
    List<PersonUseCountVo> getModelUserCount(EleParamVo eleParamVo);

    /**
     * 根据模型算子获取每个人的平均问题字数
     */
    List<ModelUserVo> getAvgUserWord(EleParamVo eleParamVo);

    /**
     * 根据模型算子获取每个模型的平均回答字数
     */
    List<ModelUserVo> getAvgModelWord(EleParamVo eleParamVo);

    /**
     * 获取每个模型的平均使用时间
     */
    List<ModelUserVo> getAvgUseTime(EleParamVo eleParamVo);

    List<ModelInfoDto> getModelInfo(Long groupId, String elementId);

    /**
     * 获取当前实验组下当前算子的模型已经有问答记录的人
     *
     * @param elementId 算子id
     * @param groupId 实验组id
     * @return
     */
    List<Long> getUserIds(String elementId, Long groupId);

    /**
     * 查询当前用户在当前算子下的聊天记录 按照时间顺序排列
     * @param elementId 算子id
     * @param groupId 实验组id
     * @param userId 用户id
     * @return
     */
    List<ModelInfoDto> getModelHistory(String elementId, Long groupId, Long userId);

}