package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.EvaluatedDimensionDao;
import com.nbtech.ailab.biz.dto.EvaluatedDimensionDto;
import com.nbtech.ailab.biz.entity.EvaluatedDimensionEntity;
import com.nbtech.ailab.biz.service.IEvaluatedDimensionService;
import com.nbtech.ailab.util.IpUtils;
import com.nbtech.common.service.impl.CrudServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 受测用户维度
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Service
@Slf4j
public class EvaluatedDimensionServiceImpl extends CrudServiceImpl<EvaluatedDimensionDao, EvaluatedDimensionEntity, EvaluatedDimensionDto> implements IEvaluatedDimensionService {

    @Override
    public QueryWrapper<EvaluatedDimensionEntity> getWrapper(EvaluatedDimensionDto dto){

        QueryWrapper<EvaluatedDimensionEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }



}