package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.BasicModelDto;
import com.nbtech.ailab.biz.entity.BasicModelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@Mapper
public interface BasicModelDao extends BaseMapper<BasicModelEntity> {

    /**
     * 获取对应的基础模型的名称
     *
     * @param id 模型id
     * @return
     */
    @Select("select name from t_basic_model where id = #{id}")
    String getBasicName(Long id);


    /**
     * 查询当前基础模型是否被模型引用
     *
     * @return
     */
    Integer getUseModelId(Long id);

    /**
     * 查询当前基础模型是否被聊天室引用
     *
     * @return
     */
    Integer getUseChatRoomId(Long id);

    /**
     * 查询自己的且已启用的基础模型集合
     * @param status 启用状态
     * @param userId 用户id
     * @return
     */
    List<BasicModelDto> getOwnerBasicModels(String status, Long userId);
}