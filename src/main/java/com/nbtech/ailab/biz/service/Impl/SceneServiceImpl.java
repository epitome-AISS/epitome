package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.SceneDao;
import com.nbtech.ailab.biz.dto.SceneDto;
import com.nbtech.ailab.biz.entity.SceneEntity;
import com.nbtech.ailab.biz.service.ISceneService;
import com.nbtech.common.service.impl.CrudServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场景
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class SceneServiceImpl extends CrudServiceImpl<SceneDao, SceneEntity, SceneDto> implements ISceneService {

    @Override
    public QueryWrapper<SceneEntity> getWrapper(SceneDto dto){

        QueryWrapper<SceneEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public List<String> getSceneList() {
        return baseDao.getSceneList();
    }
}