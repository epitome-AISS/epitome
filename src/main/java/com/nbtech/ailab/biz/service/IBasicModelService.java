package com.nbtech.ailab.biz.service;

import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.BasicModelDto;
import com.nbtech.ailab.biz.entity.BasicModelEntity;

import java.util.List;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
public interface IBasicModelService extends CrudService<BasicModelEntity, BasicModelDto> {


    /**
     * 保存基础模型
     * @param basicModelDto
     */
    void saveBasicMode(BasicModelDto basicModelDto);

    /**
     * 修改模型使用状态
     */
    void updateUseStatus(BasicModelDto dto);

    /**
     * 判断除自己以外的基础模型名称是否相同
     * @param name 名称
     * @param type 1 名称 2 英文名称 3 中文名称
     */
    Boolean judgeSameName(String name, int type);

    /**
     * 查询当前用户持有的且已开启的模型列表
     */
    List<BasicModelDto> getOwnerBasicModel();

    /**
     * 删除基础模型集合
     * @param ids 基础模型id
     */
    void deleteBasicModel(List<Long> ids);

    /**
     * 根据基础模型id验证并更新可用状态
     * @param id 基础模型id
     */
    void verifyBasicModelById(Long id);
}