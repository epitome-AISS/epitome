package com.nbtech.ailab.vo;

import com.nbtech.ailab.biz.entity.InitialPushEntity;
import com.nbtech.ailab.biz.entity.ScorePushEntity;
import com.nbtech.ailab.biz.entity.SelectPushEntity;
import lombok.Data;

import java.util.List;

@Data
public class PushDataVo {

    private String elementId;

    private List<SelectPushEntity> selectPushEntityList;

    private List<InitialPushEntity> initialPushEntityList;

    private List<ScorePushEntity> scorePushEntityList;
}
