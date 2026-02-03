package com.nbtech.ailab.biz.service;


import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.NextElementVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 实验组表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface IGroupsService extends CrudService<GroupsEntity, GroupsDto> {

    /**
     * 删除实验组数据
     *
     * @param ids 选中实验组id集合
     * @return 返回值
     */
    public BizResponse<?> deleteByIds(Long[] ids);

    List<Long> getMaterialIds();

    List<Long> getQuestionnaireIds();

    List<Long> getDialogueIds();

    /**
     * 获取json对应的数组
     *
     * @param groupId 实验组id
     * @return
     */
    List<ElementVo> getElementVo(Long groupId);

    /**
     * 统计这个算子到它前面的算子的时间 以秒做单位
     */
    Long getNextElementTime(NextElementVo nextElementVo);

    /**
     * 查询这个人做这个问卷花了多少时间
     */
    BigDecimal getElementUseTime(NextElementVo nextElementVo);

    /**
     * 获取实验组下的所有聊天室算子id
     *
     * @param groupId 实验组id
     * @return
     */
    List<String> getRoomElementIds(Long groupId);


    /**
     * 查询这个实验组下的所有素材包算子id集合(无序)
     * @param groupId 实验组id
     */
    List<String> getMaterialGroupElementId(Long groupId);

    /**
     * 通过实验组id和算子id 获取这个算子在实验组的sort
     */
    Integer getElementSort(Long groupId,String elementId);
}