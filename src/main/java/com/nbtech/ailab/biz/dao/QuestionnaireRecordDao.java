package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireExcelVo;
import com.nbtech.ailab.biz.dto.QuestionnaireInfoDto;
import com.nbtech.ailab.biz.entity.QuestionnaireRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷(填空、简答)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Mapper
public interface QuestionnaireRecordDao extends BaseMapper<QuestionnaireRecordEntity> {


    List<QuestionnaireInfoDto> getRecordData(Long groupId, Long questionnaireId, String elementId);

    void deleteByGroupsId(Long groupId);
}