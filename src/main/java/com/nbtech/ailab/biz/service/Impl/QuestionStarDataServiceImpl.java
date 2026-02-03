package com.nbtech.ailab.biz.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.SysUserDao;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireExcelVo;
import com.nbtech.ailab.biz.dto.ProcessQuestionnaireVo;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.QuestionStarElementVo;
import com.nbtech.ailab.vo.SojumpparmVo;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.QuestionStarDataDao;
import com.nbtech.ailab.biz.dto.QuestionStarDataDto;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;
import com.nbtech.ailab.biz.service.IQuestionStarDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 问卷星的问卷答题结果
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2025-04-27
 */
@Service
public class QuestionStarDataServiceImpl extends CrudServiceImpl<QuestionStarDataDao, QuestionStarDataEntity, QuestionStarDataDto> implements IQuestionStarDataService {

    @Override
    public QueryWrapper<QuestionStarDataEntity> getWrapper(QuestionStarDataDto dto) {

        QueryWrapper<QuestionStarDataEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public List<List<QuestionStarDataEntity>> getElementStarData(String processConfig) {
        List<List<QuestionStarDataEntity>> questionStarDataList = new ArrayList<>();
        // 获取所有的算子
        List<ElementVo> elementVoList = JSON.parseArray(processConfig, ElementVo.class);
        for (ElementVo elementVo : elementVoList) {
            if (!ElementTypeEnum.QUESTION_STAR.getDesc().equals(elementVo.getType())) {
                continue;
            }
            // 如果是问卷星的问卷算子
//            QuestionStarElementVo starElementVo = JSON.parseObject(elementVo.getConfig().toString(), QuestionStarElementVo.class);
            // 查询这个算子的问卷星答卷的答题结果集合
            List<QuestionStarDataEntity> questionStarDataEntities = baseDao.getQuestionStarList(elementVo.getId());
            for (QuestionStarDataEntity questionStarDataEntity : questionStarDataEntities) {
                String[] elementArr = questionStarDataEntity.getSojumpparm().split("-");
                SysUserEntity sysUserEntity = sysUserDao.selectById(elementArr[1]);
                questionStarDataEntity.setUserName(sysUserEntity.getUsername());
            }
            // 有答卷结果才做excel
            if (!questionStarDataEntities.isEmpty()) {
                questionStarDataList.add(questionStarDataEntities);
            }
        }
        return questionStarDataList;
    }

    @Override
    public List<ProcessQuestionnaireVo> getProcessQuestionnaire(Long groupId) {
        return baseDao.getProcessQuestionnaire(groupId);
    }

    @Override
    public List<ProcessQuestionnaireExcelVo> getProcessData(Long groupId, Long questionnaireId, String elementId, String processId) {
        return baseDao.getProcessData(groupId, questionnaireId, elementId, processId);
    }
}