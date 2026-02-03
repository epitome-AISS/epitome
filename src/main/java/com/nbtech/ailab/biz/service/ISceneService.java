package com.nbtech.ailab.biz.service;

import com.nbtech.ailab.biz.dto.SceneDto;
import com.nbtech.ailab.biz.entity.SceneEntity;
import com.nbtech.common.service.CrudService;

import java.util.List;

/**
 * 场景
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface ISceneService extends CrudService<SceneEntity, SceneDto> {

    /**
     * 查询所有的实验场景
     */
    List<String> getSceneList();

}