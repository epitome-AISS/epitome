package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.QuestionnaireEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问卷管理
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface QuestionnaireDao extends BaseMapper<QuestionnaireEntity> {

    Page<QuestionnairePageDto> pageQuestionnaire(Page<Object> of, QuestionnairePageDto dto, String userName, Long roleId);

    List<QuestionnaireDto> listVo(QuestionnaireDto dto, String userName);

    Page<QuestionnairePageDto> pageAudit(Page<Object> objectPage, QuestionnairePageDto dto, String userName, Long roleId);

    List<WordQuestionnaireDto> getSingleChoicesAnswers(Long groupsId, Long questionnaireId, Long questionSort);

    List<String> getMultiChoicesAnswers(Long groupsId, Long questionnaireId, Long questionSort);

    /**
     * 查询这个算子下的文字结果的集合
     * @param groupsId 实验组id
     * @param questionnaireId 问卷id
     * @param questionSort 问题序号
     * @return
     */
    List<String> getWordContextAnswers(Long groupsId, Long questionnaireId,  Long questionSort);


    /**
     * 查询这个问题的答案条数
     * @param groupsId 实验组id
     * @param questionnaireId 问卷id
     * @param questionSort 问题序号
     * @return
     */
    Integer getTextUsefully(Long groupsId, Long questionnaireId,  Long questionSort);

    /**
     * 查询这个问题的答案条数
     * @param groupsId 实验组id
     * @param questionnaireId 问卷id
     * @param questionSort 问题序号
     * @return
     */
    List<WordTypeDto> getAnswerList(Long groupsId, Long questionnaireId,  Long questionSort);

    /**
     * 统计所有的开源问卷
     */
    Integer getQuestionnaireNumber();

    /**
     * 分页查询所有status等于OPEN的问卷（联表查询收藏标识）
     *
     * @param page   分页参数
     * @param userId 用户id
     * @param questionnaireAttribution 归属人（可选）
     * @return 分页结果
     */
    Page<QuestionnaireDto> pagePublicWithFavorite(Page<Object> page, @Param("userId") Long userId, @Param("questionnaireAttribution") String questionnaireAttribution);

    /**
     * 根据id查询问卷(可以查询出已删除的数据)
     *
     * @param id 问卷id
     * @return 问卷实体
     */
    QuestionnaireEntity getQuestionnaireById(@Param("id") Long id);

    /**
     * 查询所有问卷（不限制is_deleted）
     *
     * @param dto 查询条件
     * @return 问卷列表
     */
    List<QuestionnaireDto> listAll(@Param("dto") QuestionnaireDto dto);
}