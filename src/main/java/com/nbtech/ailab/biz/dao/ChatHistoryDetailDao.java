package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ChatHistoryDetailDto;
import com.nbtech.ailab.biz.entity.ChatHistoryDetailEntity;
import com.nbtech.ailab.vo.ChatRoleSpeakCountVo;
import com.nbtech.ailab.vo.ElementWordNumberVo;
import com.nbtech.ailab.vo.RoleSpeakCountVo;
import com.nbtech.ailab.vo.RoomChatHistoryExcelVo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天记录子表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-07-11
 */
@Mapper
public interface ChatHistoryDetailDao extends BaseMapper<ChatHistoryDetailEntity> {

    /**
     * 获取当前算子的历史聊天记录
     *
     * @param elementId 算子id
     * @return
     */
    List<ChatHistoryDetailDto> getChatHistory(String elementId, String processId, Integer round);

    /**
     * 获取 上一个人发言完毕的时间
     *
     * @param elementId 算子id
     * @return
     */
    LocalDateTime getLastTalkTime(String elementId);

    /**
     * 获取 聊天室的上次更新时间
     *
     * @param elementId 算子id
     * @return
     */
    LocalDateTime getLastRoomChange(String elementId);

    /**
     * 获取当前聊天室的聊天记录
     *
     * @param elementId 算子id
     */
    List<RoomChatHistoryExcelVo> getRoomChatRecord(String elementId);

    /**
     * 获取当前聊天室各角色类型聊天次数
     *
     * @param elementId 聊天室算子id
     */
    List<ChatRoleSpeakCountVo> getChatRoleSpeakCount(String elementId);


    /**
     * 获取当前聊天室各模型的聊天次数
     *
     * @param elementId 聊天室算子id
     */
    List<ChatRoleSpeakCountVo> getChatModelSpeakCount(String elementId, String roleType);


    /**
     * 统计当前聊天室前十角色的回答次数
     */
    List<RoleSpeakCountVo> getRoleSpeakCount(String elementId);

    /**
     * 统计当前聊天室前十角色的平均发言字数
     */
    List<RoleSpeakCountVo> getAvgSpeakNum(String elementId);

    /**
     * 获取五分钟之内未被消费的消息
     *
     * @param elementId      算子id
     * @param nowTime        当前时间
     * @param roleType       角色类型不为模型
     * @param fiveMinutesAgo 五分钟之前的时间
     */
    List<ChatHistoryDetailDto> getFiveMineHistoryDetail(String elementId, String roleType, LocalDateTime nowTime, LocalDateTime fiveMinutesAgo);

    /**
     * 统计每个算子的模型字数总和
     * 查询word_number不为null且role_type为'MODEL'的聊天记录详情，通过chat_history_id关联t_chat_history获取element_id，按element_id聚合统计word_number总和
     *
     * @return 算子字数统计列表（elementId和wordNumber总和）
     */
    List<ElementWordNumberVo> getElementWordNumberSum();
}