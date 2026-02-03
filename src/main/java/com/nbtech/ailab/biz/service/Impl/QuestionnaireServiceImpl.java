package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.ModelEntity;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.constant.CommonConstant;
import com.nbtech.ailab.constant.FlowStatus;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.QuestionnaireDao;
import com.nbtech.ailab.biz.entity.QuestionnaireEntity;
import com.nbtech.ailab.biz.service.IQuestionnaireService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.util.BlankStringUtil;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 问卷管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class QuestionnaireServiceImpl extends CrudServiceImpl<QuestionnaireDao, QuestionnaireEntity, QuestionnaireDto>
        implements IQuestionnaireService {

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Override
    public QueryWrapper<QuestionnaireEntity> getWrapper(QuestionnaireDto dto) {

        QueryWrapper<QuestionnaireEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    /**
     * 问卷分页处理
     *
     * @param pageDto pageDto
     * @param dto     dto
     * @return 问卷
     */
    @Override
    public PageResult<QuestionnairePageDto> pageQuestionnaire(PageDto pageDto, QuestionnairePageDto dto,
                                                              String userName, Long roleId) {
        Page<QuestionnairePageDto> page = this.baseDao.pageQuestionnaire(
                Page.of(pageDto.getCurrent(), pageDto.getSize()).setOptimizeCountSql(false), dto, userName, roleId);
        return PageResult.build(page);
    }

    /**
     * 问卷列表展示
     *
     * @param dto dto
     * @return 列表集合
     */
    @Override
    public List<QuestionnaireDto> listVo(QuestionnaireDto dto, String userName) {
        // 校验 experimentPlanId 必填
        if (dto.getExperimentPlanId() == null) {
            throw new BizException(BizResponseCodeEnum.QUESTIONNAIRE_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY);
        }
        List<QuestionnaireDto> list = this.baseDao.listVo(dto, userName);
        return list;
    }

    @Override
    public void deleteIsReview(Long id) {
        UpdateWrapper<QuestionnaireEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("is_review", null);
        this.baseDao.update(null, updateWrapper);
    }

    /**
     * 根据名字查找数据库中已有的问卷
     *
     * @param name name
     * @return 问卷
     */
    @Override
    public QuestionnaireDto getByName(String name) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        QuestionnaireEntity questionnaire = this.baseDao.selectOne(
                Wrappers.<QuestionnaireEntity>lambdaQuery()
                        .eq(QuestionnaireEntity::getQuestionnaireName, name)
                        .eq(QuestionnaireEntity::getQuestionnaireAttribution, userName));
        return ConvertUtils.sourceToTarget(questionnaire, QuestionnaireDto.class);
    }

    @Override
    public void deleteById(Long id) {
        this.baseDao.deleteById(id);
    }

    @Override
    public PageResult<QuestionnairePageDto> pageAudit(PageDto pageDto, QuestionnairePageDto dto, String userName,
                                                      Long roleId) {
        Page<QuestionnairePageDto> page = this.baseDao.pageAudit(
                Page.of(pageDto.getCurrent(), pageDto.getSize()).setOptimizeCountSql(false), dto, userName, roleId);
        return PageResult.build(page);
    }

    @Override
    public List<QuestionnaireDto> getByIds(List<Long> ids) {
        List<QuestionnaireEntity> list = this.baseDao.selectList(
                Wrappers.<QuestionnaireEntity>lambdaQuery()
                        .in(QuestionnaireEntity::getId, ids));
        return ConvertUtils.sourceToTarget(list, QuestionnaireDto.class);
    }

    @Override
    public List<QuestionnaireDto> openList() {
        List<QuestionnaireEntity> openList = this.baseDao.selectList(
                Wrappers.<QuestionnaireEntity>lambdaQuery()
                        .eq(QuestionnaireEntity::getStatus, "OPEN"));
        return ConvertUtils.sourceToTarget(openList, QuestionnaireDto.class);
    }

    @Override
    public PageResult<QuestionnaireDto> pagePublic(PageDto pageDto, String questionnaireAttribution) {
        Long userId = ShiroUtils.getUserId();
        Page<Object> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<QuestionnaireDto> result = baseDao.pagePublicWithFavorite(page, userId, questionnaireAttribution);
        // MyBatis 会自动将 SQL 中的 0/1 转换为 Boolean false/true
        return PageResult.build(result, result.getRecords());
    }

    @Override
    public Integer getQuestionnaireNumber() {
        return baseDao.getQuestionnaireNumber();
    }

    @Override
    public QuestionnaireEntity copyQuestionnaire(Long questionnaireId, Long planId) {
        SysUserDto userEntity = ShiroUtils.getUserEntity();
        QuestionnaireEntity questionnaireEntity = baseDao.getQuestionnaireById(questionnaireId);
        questionnaireEntity.setId(null);
        // 已删除的模型也要复制出来
        questionnaireEntity.setIsDeleted(0);
        questionnaireEntity.setQuestionnaireName(questionnaireEntity.getQuestionnaireName() + "-1");

        questionnaireEntity.setQuestionnaireAttribution(userEntity.getUsername());
        questionnaireEntity.setWorkFlow(FlowStatus.DISABLE);
        questionnaireEntity.setUserId(userEntity.getId());
        Long roleId = sysUserRoleService.getByUserId(userEntity.getId());
        questionnaireEntity.setStatus(CommonConstant.DRAFT);
        questionnaireEntity.setRoleId(roleId);

        questionnaireEntity.setCreator(userEntity.getId());
        questionnaireEntity.setCreateDate(LocalDateTime.now());
        questionnaireEntity.setCreateName(userEntity.getUsername());
        questionnaireEntity.setUpdater(userEntity.getId());
        questionnaireEntity.setUpdateDate(LocalDateTime.now());
        questionnaireEntity.setUpdateName(userEntity.getUsername());
        questionnaireEntity.setUserId(userEntity.getId());

        questionnaireEntity.setExperimentPlanId(planId);

        baseDao.insert(questionnaireEntity);
        return questionnaireEntity;
    }

    @Override
    public void updateExperimentPlanId(Long id, Long experimentPlanId) {
        QuestionnaireDto questionnaireDto = get(id);
        if (questionnaireDto == null) {
            throw new BizException(BizResponseCodeEnum.GLOBAL_ERROR);
        }
        questionnaireDto.setExperimentPlanId(experimentPlanId);
        update(questionnaireDto);
    }

    @Override
    public List<QuestionnaireDto> listAll(QuestionnaireDto dto) {
        return baseDao.listAll(dto);
    }
}