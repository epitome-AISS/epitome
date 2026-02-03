package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.HistoryRecordDao;
import com.nbtech.ailab.biz.dto.HistoryRecordDto;
import com.nbtech.ailab.biz.entity.HistoryRecordEntity;
import com.nbtech.ailab.biz.service.IHistoryRecordService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 模型问答历史记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Service
public class HistoryRecordServiceImpl extends CrudServiceImpl<HistoryRecordDao, HistoryRecordEntity, HistoryRecordDto> implements IHistoryRecordService {

    @Override
    public QueryWrapper<HistoryRecordEntity> getWrapper(HistoryRecordDto dto){

        QueryWrapper<HistoryRecordEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

}