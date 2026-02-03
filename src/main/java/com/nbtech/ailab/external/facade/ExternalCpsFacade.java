package com.nbtech.ailab.external.facade;

import com.alibaba.fastjson.JSON;
import com.nbtech.ailab.biz.dao.InitialPushDao;
import com.nbtech.ailab.biz.dao.ScorePushDao;
import com.nbtech.ailab.biz.dao.SelectPushDao;
import com.nbtech.ailab.biz.dto.InitialPushDto;
import com.nbtech.ailab.biz.dto.ScorePushDto;
import com.nbtech.ailab.biz.entity.InitialPushEntity;
import com.nbtech.ailab.biz.entity.ScorePushEntity;
import com.nbtech.ailab.biz.entity.SelectPushEntity;
import com.nbtech.ailab.external.vo.InitialRequestVo;
import com.nbtech.ailab.external.vo.PushRequestVo;
import com.nbtech.ailab.external.vo.ScoreRequestVo;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExternalCpsFacade {

    @Autowired
    private InitialPushDao initialPushDao;

    @Autowired
    private SelectPushDao selectPushDao;

    @Autowired
    private ScorePushDao scorePushDao;

    /**
     * 合作测评界面初始化
     *
     * @param element_id
     * @param user_id
     * @param role
     * @param scene
     */
    public void initial(String element_id, String user_id, String role, String scene) {

    }

    /**
     * 初始表单数据推送
     */
    public void push(InitialRequestVo request) {
        InitialPushEntity initialPushEntity = new InitialPushEntity();
        initialPushEntity.setElementId(request.getElement_id());
        initialPushEntity.setUserId(request.getUser_id());
        initialPushEntity.setIdentity(request.getIdentity());
        initialPushEntity.setData(JSON.toJSONString(request.getData()));
        initialPushDao.insert(initialPushEntity);
    }

    /**
     * 选择结果推送
     */
    public void selectPush(PushRequestVo request) {
        SelectPushEntity selectPushEntity = new SelectPushEntity();
        selectPushEntity.setElementId(request.getElement_id());
        selectPushEntity.setUserId(request.getUser_id());
        selectPushEntity.setData(JSON.toJSONString(request.getData()));
        selectPushDao.insert(selectPushEntity);
    }

    /**
     * 测评结果推送
     * @param request
     */
    public void scorePush(ScoreRequestVo request){
        ScorePushEntity scorePushEntity = new ScorePushEntity();
        scorePushEntity.setElementId(request.getElement_id());
        scorePushEntity.setUserId(request.getUser_id());
        scorePushEntity.setData(JSON.toJSONString(request.getData()));
        scorePushDao.insert(scorePushEntity);
    }



}
