package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.ScorePushDao;
import com.nbtech.ailab.biz.dto.ScorePushDto;
import com.nbtech.ailab.biz.entity.ScorePushEntity;
import com.nbtech.ailab.biz.service.IScorePushService;
import org.springframework.stereotype.Service;

/**
 * 测评结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-06-05
 */
@Service
public class ScorePushServiceImpl extends CrudServiceImpl<ScorePushDao, ScorePushEntity, ScorePushDto> implements IScorePushService {

    @Override
    public QueryWrapper<ScorePushEntity> getWrapper(ScorePushDto dto){

        QueryWrapper<ScorePushEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}