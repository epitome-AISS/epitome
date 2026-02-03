package com.nbtech.ailab.biz.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.dto.ParamDto;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.vo.ExperimentGroupVo;
import com.nbtech.ailab.vo.ExperimentVo;
import com.nbtech.ailab.vo.NextElementVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 实验组表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface GroupsDao extends BaseMapper<GroupsEntity> {

    /**
     * 通过实验计划获取实验组
     *
     * @param planId 实验计划
     * @return
     */
    Page<GroupsEntity> getGroupsByPlan(Page<String> page, String planId);

    /**
     * 通过实验组id获取秘钥
     */
    String getKeyString(Long groupId);

    /**
     * 查询实验计划信息
     */
    ExperimentPlanEntity getExpermentPlan(Long experimentCode);

    /**
     * 删除实验组的同时删除实验组人群包
     */
    void deleteByIds(Long[] ids);

    /**
     * 统计实验计划下的所有实验人数
     */
    Integer sumPersonNumber(Long experimentId);

    List<Long> getMaterialIds();

    List<Long> getQuestionnaireIds();

    List<Long> getDialogueIds();

    /**
     * 获取实验组已经发布下的问卷算子集合
     *
     * @return 实验组已经发布下的问卷算子集合
     */
    List<ParamDto> getGroupsHaveQuestionnaireIds();

    /**
     * 获取实验组已经发布的模型对话算子集合
     *
     * @return 实验组已经发布的模型对话算子集合
     */
    List<ParamDto> getGroupsHaveDialogueIds();

    /**
     * 通过用户id获取用户的实验信息
     */
    List<ExperimentVo> getExperiment(Long userId);

    /**
     * 通过实验获取实验组id
     */
    List<Long> getGroupIdList(Long planId);

    /**
     * 查询这个实验组下的所有素材包算子id集合(无序)
     * @param groupId 实验组id
     * @return
     */
    List<String> getMaterialGroupElementId(Long groupId);

    /**
     * 根据实验组id获取实验计划名称和实验组名称
     *
     * @param experimentId 实验id
     */
    ExperimentGroupVo getExperimentGroupName(Long experimentId);

    String getElementById(Long groupId, Long experimentId, Long questionnaireId);

    /**
     * 统计这个算子到它前面的算子的时间 以秒做单位
     */
    LocalDateTime getNextElementTime(NextElementVo nextElementVo);

    List<ParamDto> getGroupsHaveInterveneIds();

    List<GroupsDto> getProcessConfigStrings(Long materialId);

    /**
     * 获取实验组下所有的聊天室算子id
     *
     * @param groupId 实验组id
     * @return
     */
    List<String> getRoomElementId(Long groupId);

    /**
     * 通过实验组id获取实验计划场景
     */
    String getExperimentScene(Long groupId);

    /**
     * 实验组添加人数
     */
    void addGroupPerson(Integer number, Long groupId);

}