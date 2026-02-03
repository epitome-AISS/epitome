package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireExcelVo;
import com.nbtech.ailab.biz.dto.QuestionnaireInfoDto;
import com.nbtech.ailab.biz.entity.QuestionnaireScaleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷(量表)
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-09
 */
@Mapper
public interface QuestionnaireScaleDao extends BaseMapper<QuestionnaireScaleEntity> {

    List<QuestionnaireInfoDto> getScaleData(Long groupId, Long questionnaireId, String elementId);

    void deleteByGroupsId(Long groupId);

}