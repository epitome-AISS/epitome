package com.nbtech.ailab.biz.service;

import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.vo.TagVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.ModelDto;
import com.nbtech.ailab.biz.entity.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * 模型对话管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-11
 */
public interface IModelService extends CrudService<ModelEntity, ModelDto> {
    ModelDto getByModelName(String modelName);

    List<TagVo> listVo(ModelDto dto);

    PageResult<ModelDto> pageModel(PageDto pageDto, ModelDto dto);

    void deleteById(Long id);

    PageResult<ModelDto> pageAudit(PageDto pageDto, ModelDto dto);

    /**
     * 获取modelData数据
     */
    ModelDto getById(Long id);

    List<ModelDto> openList(Integer modelBotType);

    /**
     * 查询所有模型的所有标签
     *
     * @return
     */
    List<String> getModelTags();

    /**
     * 分页查询所有status等于OPEN的模型，如果传递了attribution则同时按该条件查询
     *
     * @param pageDto     分页参数
     * @param attribution 归属人（可选）
     * @return 模型分页结果
     */
    PageResult<ModelDto> pagePublic(PageDto pageDto, String attribution);

    /**
     * 模型复制
     *
     * @param modelId
     * @return
     */
    ModelEntity copyModel(Long modelId, Long planId);

    /**
     * 查询所有模型（不限制is_delete）
     *
     * @param dto 查询条件
     * @return 模型列表
     */
    List<ModelDto> listAll(ModelDto dto);
}