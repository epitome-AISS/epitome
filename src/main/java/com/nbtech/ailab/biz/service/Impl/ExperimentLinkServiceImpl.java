package com.nbtech.ailab.biz.service.Impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.ExperimentLinkDao;
import com.nbtech.ailab.biz.dto.ExperimentLinkDto;
import com.nbtech.ailab.biz.entity.ExperimentLinkEntity;
import com.nbtech.ailab.biz.service.IExperimentLinkService;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 实验链接
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-11
 */
@Service
public class ExperimentLinkServiceImpl extends CrudServiceImpl<ExperimentLinkDao, ExperimentLinkEntity, ExperimentLinkDto> implements IExperimentLinkService {

    @Override
    public QueryWrapper<ExperimentLinkEntity> getWrapper(ExperimentLinkDto dto) {

        QueryWrapper<ExperimentLinkEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    /**
     * 根据实验计划id 实验组id获取生成的实验链接列表
     *
     * @param experimentPlanId
     * @param groupsId
     * @return
     */
    @Override
    public List<String> getList(Long experimentPlanId, Long groupsId) {
        List<ExperimentLinkEntity> experimentLinkEntities = this.baseDao.selectList(Wrappers.<ExperimentLinkEntity>lambdaQuery()
                .eq(ExperimentLinkEntity::getExperimentPlanId, experimentPlanId)
                .eq(ExperimentLinkEntity::getGroupsId, groupsId)
        );

        if (CollectionUtil.isEmpty(experimentLinkEntities)) {
            return null;
        }

        List<String> linkNames = experimentLinkEntities.stream().map(ExperimentLinkEntity::getLinkName).collect(Collectors.toList());
        return linkNames;
    }

    @Override
    public List<ExperimentLinkDto> getByGroupId(Long groupsId) {
        List<ExperimentLinkEntity> experimentLinkEntities = this.baseDao.selectList(Wrappers.<ExperimentLinkEntity>lambdaQuery()
                .eq(ExperimentLinkEntity::getGroupsId, groupsId)
        );

        return ConvertUtils.sourceToTarget(experimentLinkEntities, ExperimentLinkDto.class);
    }
}