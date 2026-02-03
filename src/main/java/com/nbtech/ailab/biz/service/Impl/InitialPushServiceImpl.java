package com.nbtech.ailab.biz.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.ScorePushDao;
import com.nbtech.ailab.biz.dao.SelectPushDao;
import com.nbtech.ailab.biz.entity.ScorePushEntity;
import com.nbtech.ailab.biz.entity.SelectPushEntity;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.PushDataVo;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.InitialPushDao;
import com.nbtech.ailab.biz.dto.InitialPushDto;
import com.nbtech.ailab.biz.entity.InitialPushEntity;
import com.nbtech.ailab.biz.service.IInitialPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Service
public class InitialPushServiceImpl extends CrudServiceImpl<InitialPushDao, InitialPushEntity, InitialPushDto> implements IInitialPushService {

    @Autowired
    private SelectPushDao selectPushDao;

    @Autowired
    private ScorePushDao scorePushDao;

    @Override
    public QueryWrapper<InitialPushEntity> getWrapper(InitialPushDto dto){

        QueryWrapper<InitialPushEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Override
    public List<PushDataVo> getPushData(String processConfig) {
        List<ElementVo> elementVoList = JSON.parseArray(processConfig, ElementVo.class);
        // 查询所有的推送数据
        List<PushDataVo> pushDataVoList = new ArrayList<>();
        for (ElementVo elementVo : elementVoList) {
            PushDataVo pushDataVo = new PushDataVo();
            // 不是合作测评算子
//            if (!ElementTypeEnum.COOPERATIVE.getDesc().equals(elementVo.getType())) {
//                continue;
//            }
            // 选择推送数据集合
            List<SelectPushEntity> selectPushEntities = selectPushDao.getPushData(elementVo.getId());
            if (!selectPushEntities.isEmpty()){
                pushDataVo.setElementId(elementVo.getId());
                pushDataVo.setSelectPushEntityList(selectPushEntities);
            }
            // 初始化推送数据集合
            List<InitialPushEntity> initialPushEntities = baseDao.getInitialData(elementVo.getId());
            if (!initialPushEntities.isEmpty()){
                pushDataVo.setElementId(elementVo.getId());
                pushDataVo.setInitialPushEntityList(initialPushEntities);
            }
            List<ScorePushEntity> scoreDataEntities = scorePushDao.getScoreData(elementVo.getId());
            if (!initialPushEntities.isEmpty()){
                pushDataVo.setElementId(elementVo.getId());
                pushDataVo.setScorePushEntityList(scoreDataEntities);
            }
            // 测评结果推送数据集合
            if (pushDataVo.getElementId() != null){
                pushDataVoList.add(pushDataVo);
            }
        }
        return pushDataVoList;
    }
}