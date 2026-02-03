package com.nbtech.ailab.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.QuestionnairePageDto;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;
import com.nbtech.ailab.biz.dto.QuestionnaireDto;
import com.nbtech.ailab.biz.entity.QuestionnaireEntity;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 问卷管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface IQuestionnaireService extends CrudService<QuestionnaireEntity, QuestionnaireDto> {

    PageResult<QuestionnairePageDto> pageQuestionnaire(PageDto pageDto, QuestionnairePageDto dto, String userName, Long roleId);

    List<QuestionnaireDto> listVo(QuestionnaireDto dto, String userName);

    void deleteIsReview(Long id);

    QuestionnaireDto getByName(String name);

    void deleteById(Long id);

    PageResult<QuestionnairePageDto> pageAudit(PageDto pageDto, QuestionnairePageDto dto, String userName, Long roleId);

    List<QuestionnaireDto> getByIds(List<Long> ids);

    List<QuestionnaireDto> openList();

    /**
     * 分页查询所有status等于OPEN的问卷，如果传递了questionnaireAttribution则同时按该条件查询
     *
     * @param pageDto                  分页参数
     * @param questionnaireAttribution 归属人（可选）
     * @return 问卷分页结果
     */
    PageResult<QuestionnaireDto> pagePublic(PageDto pageDto, String questionnaireAttribution);

    /**
     * 统计所有的开源问卷
     */
    Integer getQuestionnaireNumber();

    /**
     * 复制问卷
     *
     * @param questionnaireId
     * @return
     */
    QuestionnaireEntity copyQuestionnaire(Long questionnaireId, Long planId);

    /**
     * 修改问卷的实验计划id
     *
     * @param id 问卷id
     * @param experimentPlanId 实验计划id
     */
    void updateExperimentPlanId(Long id, Long experimentPlanId);

    /**
     * 查询所有问卷（不限制is_deleted）
     *
     * @param dto 查询条件
     * @return 问卷列表
     */
    List<QuestionnaireDto> listAll(QuestionnaireDto dto);
}