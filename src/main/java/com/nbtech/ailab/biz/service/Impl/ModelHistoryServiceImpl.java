package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.ModelHistoryDao;
import com.nbtech.ailab.biz.dto.ModelHistoryDto;
import com.nbtech.ailab.biz.entity.ModelHistoryEntity;
import com.nbtech.ailab.biz.service.IModelHistoryService;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.common.LanguageEnum;
import com.nbtech.ailab.util.MessageUtil;
import com.nbtech.ailab.vo.ElementParamVo;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.InterventionVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 模型问答历史
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@Service
public class ModelHistoryServiceImpl extends CrudServiceImpl<ModelHistoryDao, ModelHistoryEntity, ModelHistoryDto> implements IModelHistoryService {



    @Override
    public QueryWrapper<ModelHistoryEntity> getWrapper(ModelHistoryDto dto){

        QueryWrapper<ModelHistoryEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }



}