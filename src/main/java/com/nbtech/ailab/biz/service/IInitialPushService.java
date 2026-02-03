package com.nbtech.ailab.biz.service;

import com.nbtech.ailab.vo.PushDataVo;
import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.InitialPushDto;
import com.nbtech.ailab.biz.entity.InitialPushEntity;

import java.util.List;

/**
 * 初始化推送数据表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
public interface IInitialPushService extends CrudService<InitialPushEntity, InitialPushDto> {

    /**
     * 根据算子配置 查询相关的算子推送数据
     */
    List<PushDataVo> getPushData(String processConfig);

}