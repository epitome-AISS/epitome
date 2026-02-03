package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.QuestionnaireDataDao;
import com.nbtech.ailab.biz.dto.QuestionnaireDataDto;
import com.nbtech.ailab.biz.entity.QuestionnaireDataEntity;
import com.nbtech.ailab.biz.service.IQuestionnaireDataService;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 问卷数据
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class QuestionnaireDataServiceImpl extends CrudServiceImpl<QuestionnaireDataDao, QuestionnaireDataEntity, QuestionnaireDataDto> implements IQuestionnaireDataService {

    @Override
    public QueryWrapper<QuestionnaireDataEntity> getWrapper(QuestionnaireDataDto dto) {

        QueryWrapper<QuestionnaireDataEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public List<Long> getUserIds(Long groupId, Long questionnaireId) {
        List<QuestionnaireDataEntity> questionnaireDataEntities = this.baseDao.selectList(
                Wrappers.<QuestionnaireDataEntity>lambdaQuery()
                        .eq(QuestionnaireDataEntity::getGroupsId, groupId)
                        .eq(QuestionnaireDataEntity::getQuestionnaireId, questionnaireId));
        List<Long> userIds = questionnaireDataEntities.stream().map(QuestionnaireDataEntity::getUserId).collect(Collectors.toList());
        return userIds;
    }

}