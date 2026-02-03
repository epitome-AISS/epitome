package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.SelectPushDao;
import com.nbtech.ailab.biz.dto.SelectPushDto;
import com.nbtech.ailab.biz.entity.SelectPushEntity;
import com.nbtech.ailab.biz.service.ISelectPushService;
import org.springframework.stereotype.Service;

/**
 * 选择结果推送数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-05-07
 */
@Service
public class SelectPushServiceImpl extends CrudServiceImpl<SelectPushDao, SelectPushEntity, SelectPushDto> implements ISelectPushService {

    @Override
    public QueryWrapper<SelectPushEntity> getWrapper(SelectPushDto dto){

        QueryWrapper<SelectPushEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}