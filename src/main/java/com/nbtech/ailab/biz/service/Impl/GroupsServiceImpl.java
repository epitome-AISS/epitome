package com.nbtech.ailab.biz.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.QuestionnaireDataDao;
import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.biz.entity.QuestionnaireDataEntity;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.NextElementVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 实验组表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class GroupsServiceImpl extends CrudServiceImpl<GroupsDao, GroupsEntity, GroupsDto> implements IGroupsService {

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private QuestionnaireDataDao questionnaireDataDao;

    @Override
    public QueryWrapper<GroupsEntity> getWrapper(GroupsDto dto) {
        QueryWrapper<GroupsEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public BizResponse<?> deleteByIds(Long[] ids) {
        // 删除实验组时同步删除实验人群包
        baseDao.deleteByIds(ids);
        return BizResponse.success();
    }

    /**
     * 查询实验组中已经被使用的素材
     *
     * @return 素材id
     */
    @Override
    public List<Long> getMaterialIds() {
        List<Long> materialIds = this.baseDao.getMaterialIds();
        return materialIds;
    }

    /**
     * 查询实验组中已经被使用的问卷
     *
     * @return 问卷id
     */
    @Override
    public List<Long> getQuestionnaireIds() {
        List<Long> questionnaireIds = this.baseDao.getQuestionnaireIds();
        return questionnaireIds;
    }

    /**
     * 查询实验组中已经被使用的模型对话
     *
     * @return 模型对话id
     */
    @Override
    public List<Long> getDialogueIds() {
        List<Long> dialogueIds = this.baseDao.getDialogueIds();
        return dialogueIds;
    }

    /**
     * 获取json对应的算子
     *
     * @param groupId 实验组id
     * @return
     */
    @Override
    public List<ElementVo> getElementVo(Long groupId) {
        GroupsEntity groupsEntity = groupsDao.selectById(groupId);
        if (!Optional.ofNullable(groupsEntity).isPresent()) {
            // 实验组不存在
            throw new BizException(BizResponseCodeEnum.EXISTS_NOT_GROUP);
        }
        String jsonString = groupsEntity.getProcessConfig();
        List<ElementVo> elementVoList = new ArrayList<>();
        try {
            elementVoList = JSON.parseArray(jsonString, ElementVo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(elementVoList);
        return elementVoList;
    }

    @Override
    public Long getNextElementTime(NextElementVo nextElementVo) {
        ElementVo elementVo = getLastElementVo(nextElementVo);
        if (elementVo == null) {
            // 当前算子不存在或者当前算子是第一个算子
            return null;
        }
        // 这个算子的最新创建时间
        LocalDateTime thisTime = groupsDao.getNextElementTime(nextElementVo);
        if (thisTime == null) {
            return null;
        }
        // 上个算子的最新创建时间
        nextElementVo.setElementId(elementVo.getId());
        LocalDateTime lastTime = groupsDao.getNextElementTime(nextElementVo);
        if (lastTime == null) {
            return null;
        }
        long seconds = ChronoUnit.SECONDS.between(lastTime, thisTime);
        ;
        return seconds;
    }

    @Override
    public BigDecimal getElementUseTime(NextElementVo nextElementVo) {
        QuestionnaireDataEntity questionnaireDataEntity = questionnaireDataDao.selectOne(Wrappers.<QuestionnaireDataEntity>lambdaQuery()
                .eq(QuestionnaireDataEntity::getElementId, nextElementVo.getElementId())
                .eq(QuestionnaireDataEntity::getGroupsId, nextElementVo.getGroupsId())
                .eq(QuestionnaireDataEntity::getUserId, nextElementVo.getUserId())
                .last("limit 1"));
        return questionnaireDataEntity.getUseTime() != null ? questionnaireDataEntity.getUseTime() : BigDecimal.ZERO;
    }

    @Override
    public List<String> getRoomElementIds(Long groupId) {
        return groupsDao.getRoomElementId(groupId);
    }



    /**
     * 查询当前算子的上一个算子
     */
    ElementVo getLastElementVo(NextElementVo nextElementVo) {
        List<ElementVo> elementVoList = getElementVo(nextElementVo.getGroupsId());
        Iterator<ElementVo> iterable = elementVoList.iterator();
        Long index = -1L;
        // 获取当前算子下标
        while (iterable.hasNext()) {
            ElementVo thisElementVo = iterable.next();
            if (thisElementVo.getId().equals(String.valueOf(nextElementVo.getElementId()))) {
                index = thisElementVo.getSequence();
                break;
            }
        }
        // 没有检索到当前算子
        if (index == -1) {
            return null;
        }
        // 当前算子是第一个算子
        if (index == 0) {
            return null;
        }
        // 获取上一算子
        return elementVoList.get((int) (index - 1L));
    }




    @Override
    public List<String> getMaterialGroupElementId(Long groupId) {
        return baseDao.getMaterialGroupElementId(groupId);
    }

    @Override
    public Integer getElementSort(Long groupId, String elementId) {
        List<ElementVo> elementVo = getElementVo(groupId);
        Optional<ElementVo> target = elementVo.stream().filter(x -> x.getId().equals(elementId)).findFirst();
        if (target.isPresent()) {
            Long sequence = target.map(ElementVo::getSequence).orElse(null);
            return sequence.intValue();
        }else {
            return null;
        }
    }

}