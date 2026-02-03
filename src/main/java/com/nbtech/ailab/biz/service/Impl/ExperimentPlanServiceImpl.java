package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dao.ExperimentPlanDao;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.GroupsPersonDao;
import com.nbtech.ailab.biz.dao.MaterialDao;
import com.nbtech.ailab.biz.dao.ModelDao;
import com.nbtech.ailab.biz.dto.ExperimentPlanDto;
import com.nbtech.ailab.biz.dto.ExperimentPlanSimpleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.biz.entity.GroupsPersonEntity;
import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.CompletedStatusEnum;
import com.nbtech.ailab.common.HoldStatusEnum;
import com.nbtech.ailab.common.PlanStatusEnum;
import com.nbtech.ailab.util.BlankStringUtil;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.HomeRecordVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 实验表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class ExperimentPlanServiceImpl extends
        CrudServiceImpl<ExperimentPlanDao, ExperimentPlanEntity, ExperimentPlanDto> implements IExperimentPlanService {

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Override
    public QueryWrapper<ExperimentPlanEntity> getWrapper(ExperimentPlanDto dto) {

        QueryWrapper<ExperimentPlanEntity> wrapper = new QueryWrapper<>();

        wrapper.eq(BlankStringUtil.isBlank(dto.getExperimentStatus()), "experiment_status", dto.getExperimentStatus());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentCode()), "experiment_code", dto.getExperimentCode());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentScene()), "experiment_scene", dto.getExperimentScene());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentContact()), "experiment_contact",
                dto.getExperimentContact());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentName()), "experiment_name", dto.getExperimentName());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentField()), "experiment_field", dto.getExperimentField());
        // 发布时间范围查询：大于等于开始时间，小于等于结束时间
        wrapper.ge(dto.getPublishTimeStart() != null, "publish_time", dto.getPublishTimeStart());
        wrapper.le(dto.getPublishTimeEnd() != null, "publish_time", dto.getPublishTimeEnd());
        wrapper.eq("is_deleted", 0);

        /**
         * 根据用户id获取角色集合
         */
        Long userId = ShiroUtils.getUserEntity().getId();
        SysRoleEntity role = sysRoleService.getRoleByUserId(userId);

        if (role != null && role.getTag() != null && role.getTag() == 1) {
            // 1 是超级管理员 全部都能看
        } else if (role != null && role.getId() == 4) {
            if (dto.getExperimentStatus() == null) {
                wrapper.eq("holding_status", HoldStatusEnum.PUBLIC.getDesc())
                        .or()
                        .eq("experiment_status", PlanStatusEnum.BEAUDIT.getDesc());
            } else if (PlanStatusEnum.BEAUDIT.getDesc().equals(dto.getExperimentStatus())) {
                // id = 4 审核者 只能看未审核和已开源的
                // wrapper.eq("experiment_status",PlanStatusEnum.BEAUDIT.getDesc());
            } else if (PlanStatusEnum.END.getDesc().equals(dto.getExperimentStatus())) {
                wrapper.eq("holding_status", HoldStatusEnum.PUBLIC.getDesc());
            } else {
                // 审核者点击待审核完成以外的状态就啥也不显示
                wrapper.eq("experiment_status", null);
            }
        } else {
            // 没有权限的账号
            wrapper.and(w -> w.eq("experiment_attribution", ShiroUtils.getUserEntity().getUsername())
                    .or()
                    .eq("creator", userId));
        }

        wrapper.orderByDesc("update_date");
        wrapper.orderByDesc("id");
        return wrapper;

    }

    @Override
    public PageResult<ExperimentPlanDto> pagePlan(PageDto pageDto, ExperimentPlanDto dto) {
        PageResult<ExperimentPlanDto> pageResult = super.page(pageDto, dto);
        // 计算并填充百分比进度和实验组数量
        for (ExperimentPlanDto planDto : pageResult.getRecords()) {
            BigDecimal experimentProgress = calculateExperimentProgress(planDto.getId());
            planDto.setExperimentProgress(experimentProgress);
            // 统计实验计划下的实验组数量
            Long haveGroup = groupsDao.selectCount(Wrappers.<GroupsEntity>lambdaQuery()
                    .eq(GroupsEntity::getExperimentId, planDto.getId()));
            planDto.setHaveGroupNumber(haveGroup != null ? haveGroup.intValue() : 0);
        }
        return pageResult;
    }

    /**
     * 计算实验计划的百分比进度
     * 
     * @param planId 实验计划id
     * @return 百分比进度（0-100）
     */
    private BigDecimal calculateExperimentProgress(Long planId) {
        // 完成人数：experiment_id = planId 且 end_time is not null
        Long finishedNumber = groupsPersonDao.selectCount(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getExperimentId, planId)
                        .isNotNull(GroupsPersonEntity::getEndTime));

        // 总人数：experiment_id = planId
        Long totalNumber = groupsPersonDao.selectCount(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getExperimentId, planId));

        // 计算百分比（保留两位小数）
        BigDecimal experimentProgress = BigDecimal.ZERO;
        if (totalNumber != null && totalNumber > 0) {
            experimentProgress = BigDecimal.valueOf(finishedNumber == null ? 0L : finishedNumber)
                    .divide(BigDecimal.valueOf(totalNumber), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return experimentProgress;
    }

    @Override
    public ExperimentPlanDto getPlanById(Long id) {

        ExperimentPlanDto experimentPlanDto = get(id);
        // 统计存在的实验组数量
        Long haveGroup = groupsDao.selectCount(Wrappers.<GroupsEntity>lambdaQuery()
                .eq(GroupsEntity::getExperimentId, id));
        // 统计实验组人数和
        Integer sumPerson = groupsDao.sumPersonNumber(id);
        experimentPlanDto.setHaveGroupNumber(haveGroup.intValue());
        experimentPlanDto.setExperimentPersonNumber(sumPerson);
        return experimentPlanDto;
    }

    @Override
    public Long getByExperimentName(String experimentName) {
        ExperimentPlanEntity experimentPlanEntity = this.baseDao.selectOne(Wrappers.<ExperimentPlanEntity>lambdaQuery()
                .eq(ExperimentPlanEntity::getExperimentName, experimentName));
        Long experimentId = experimentPlanEntity.getId();
        return experimentId;
    }

    @Override
    public HomeRecordVo getHomeRecord() {
        HomeRecordVo vo = baseDao.getHomeRecord(PlanStatusEnum.BEEND.getDesc(),
                PlanStatusEnum.END.getDesc(),
                CompletedStatusEnum.END.getDesc());
        Integer models = modelDao.getOpenList();
        Integer questionnaires = questionnaireService.openList().size();
        Integer materials = materialDao.getOpenList().size();
        vo.setOpenNumber(models + questionnaires + materials);
        return vo;
    }

    @Override
    public String getOldCode() {
        return baseDao.getOldCode();
    }

    @Override
    public void deleteExperimentPlan(Long[] ids) {
        for (Long id : ids) {
            // 删除实验计划
            baseDao.deleteById(id);
            // 删除实验组
            groupsDao.delete(Wrappers.<GroupsEntity>lambdaQuery()
                    .eq(GroupsEntity::getExperimentId, id));
        }
    }

    /**
     * 构建基础查询条件（不包含权限逻辑）
     */
    private QueryWrapper<ExperimentPlanEntity> buildBaseWrapper(ExperimentPlanDto dto) {
        QueryWrapper<ExperimentPlanEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(BlankStringUtil.isBlank(dto.getExperimentStatus()), "experiment_status", dto.getExperimentStatus());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentCode()), "experiment_code", dto.getExperimentCode());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentScene()), "experiment_scene", dto.getExperimentScene());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentContact()), "experiment_contact",
                dto.getExperimentContact());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentName()), "experiment_name", dto.getExperimentName());
        wrapper.like(BlankStringUtil.isBlank(dto.getExperimentField()), "experiment_field", dto.getExperimentField());
        wrapper.eq(BlankStringUtil.isBlank(dto.getExperimentAttribution()), "experiment_attribution",
                dto.getExperimentAttribution());
        // 发布时间范围查询：大于等于开始时间，小于等于结束时间
        wrapper.ge(dto.getPublishTimeStart() != null, "publish_time", dto.getPublishTimeStart());
        wrapper.le(dto.getPublishTimeEnd() != null, "publish_time", dto.getPublishTimeEnd());
        wrapper.eq("is_deleted", 0);
        wrapper.orderByDesc("update_date");
        wrapper.orderByDesc("id");
        return wrapper;
    }

    @Override
    public PageResult<ExperimentPlanDto> pagePublic(PageDto pageDto, ExperimentPlanDto dto) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<ExperimentPlanDto> result = baseDao.pagePublicWithFavorite(page, dto, userId);
        // MyBatis 会自动将 SQL 中的 0/1 转换为 Boolean false/true
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public List<ExperimentPlanSimpleDto> listMyPlans() {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long userId = user.getId();

        QueryWrapper<ExperimentPlanEntity> wrapper = new QueryWrapper<>();
        wrapper.select("id", "experiment_name", "experiment_title", "create_date")
                .eq("is_deleted", 0);

        // 根据用户角色过滤，与 getWrapper 逻辑一致
        SysRoleEntity role = sysRoleService.getRoleByUserId(userId);
        if (role != null && role.getTag() != null && role.getTag() == 1) {
            // 超级管理员 全部都能看
        } else if (role != null && role.getId() == 4) {
            // 审核者 只能看已开源或待审核的
            wrapper.and(w -> w.eq("holding_status", HoldStatusEnum.PUBLIC.getDesc())
                    .or()
                    .eq("experiment_status", PlanStatusEnum.BEAUDIT.getDesc()));
        } else {
            // 普通用户 只能看自己归属或创建的
            wrapper.and(w -> w.eq("experiment_attribution", userName)
                    .or()
                    .eq("creator", userId));
        }

        wrapper.orderByDesc("update_date");

        List<ExperimentPlanEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, ExperimentPlanSimpleDto.class);
    }

    @Override
    public List<ExperimentPlanSimpleDto> listMyPublicPlans(String experimentName) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();

        QueryWrapper<ExperimentPlanEntity> wrapper = new QueryWrapper<>();
        wrapper.select("id", "experiment_name", "experiment_title", "create_date")
                .eq("experiment_attribution", userName)
                .eq("holding_status", HoldStatusEnum.PRIVATE.getDesc())
                .eq("is_deleted", 0);

        if (experimentName != null && !experimentName.trim().isEmpty()) {
            wrapper.like("experiment_name", experimentName);
        }

        wrapper.orderByDesc("update_date");

        List<ExperimentPlanEntity> entities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entities, ExperimentPlanSimpleDto.class);
    }

}