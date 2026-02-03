package com.nbtech.ailab.biz.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.AddressTotalDto;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.dto.UserInfoDto;
import com.nbtech.ailab.biz.entity.GroupsPersonEntity;
import com.nbtech.ailab.vo.ExperimentTotalVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface GroupsPersonDao extends BaseMapper<GroupsPersonEntity> {

    /**
     * 查询当前用户所属的实验未完成的人数
     */
    Long getUserExperimentId(Long userId, Long planId, String status);

    /**
     * 查询当前用户所属的实验计划id
     */
    Long getExperimentId(Long userId);

    List<AddressTotalDto> getAddressByGroupId(Long id);

    List<LocalDate> getCommonDate(Long groupId);

    /**
     * 获取完成人数
     */
    Long getFinishedNumber(Long groupId, LocalDate commonDate);

//    /**
//     * 获取进行中人数
//     */
//    Long getProcessingNumber(Long groupId);

    /**
     * 获取累计完成人数
     */
    Long getSumNumber(Long groupId, LocalDate currentDay);

    /**
     * 获取进行中人数
     */
    Long getProcessingNumber(Long groupId);

    List<UserInfoDto> getUserInfo(Long experimentId, Long groupId);

    /**
     * 查询在进行中的实验的实验组id集合
     *
     * @param status 进行中的状态
     * @return
     */
    List<Long> getGroupsList(String status);

    /**
     * 统计实验组昨天有没有开始实验
     *
     * @param groupId   实验组id
     * @param yesterday 昨天的日期
     * @return
     */
    Long countGroupStart(Long groupId, LocalDate yesterday);

    /**
     * 统计实验组进行中的人数
     *
     * @param groupId    实验组id
     * @param currentDay 统计日期
     * @return
     */
    Long countProcessing(Long groupId, LocalDate currentDay);

    /**
     * 获取同组的用户的id集合
     *
     * @param groupId 实验组id
     * @return
     */
    List<Long> getGroupUserList(Long groupId);

    /**
     * 获取用户的实验组id
     *
     * @param userId 用户id
     * @return
     */
    Long getUserGroupId(Long userId);
}