package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.GroupsPersonDao;
import com.nbtech.ailab.biz.entity.GroupsPersonEntity;
import com.nbtech.ailab.common.CompletedStatusEnum;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.ExperimentMessageDao;
import com.nbtech.ailab.biz.dto.ExperimentMessageDto;
import com.nbtech.ailab.biz.entity.ExperimentMessageEntity;
import com.nbtech.ailab.biz.service.IExperimentMessageService;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Watchable;
import java.time.LocalDate;
import java.util.List;

/**
 * 实验组信息表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-06-11
 */
@Service
public class ExperimentMessageServiceImpl extends CrudServiceImpl<ExperimentMessageDao, ExperimentMessageEntity, ExperimentMessageDto> implements IExperimentMessageService {

    @Autowired
    private ExperimentMessageDao experimentMessageDao;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Override
    public QueryWrapper<ExperimentMessageEntity> getWrapper(ExperimentMessageDto dto) {

        QueryWrapper<ExperimentMessageEntity> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    /**
     * 根据实验组id获取对应的实验组分析信息
     *
     * @param groupId groupId
     * @return 实验组信息列表
     */
    @Override
    public List<ExperimentMessageDto> getByGroupId(Long groupId) {
        List<ExperimentMessageEntity> experimentMessageEntities = this.baseDao.selectList(
                Wrappers.<ExperimentMessageEntity>lambdaQuery()
                        .eq(ExperimentMessageEntity::getGroupId, groupId)
                        .orderByAsc(ExperimentMessageEntity::getRecordDate));
        return ConvertUtils.sourceToTarget(experimentMessageEntities, ExperimentMessageDto.class);
    }

    /**
     * 获取实验组下已存在的记录
     *
     * @param groupId groupId
     * @param date    date
     * @return ExperimentMessageDto
     */
    @Override
    public ExperimentMessageDto getByDate(Long groupId, LocalDate date) {
        ExperimentMessageEntity experimentMessageEntity = this.baseDao.selectOne(
                Wrappers.<ExperimentMessageEntity>lambdaQuery()
                        .eq(ExperimentMessageEntity::getGroupId, groupId)
                        .eq(ExperimentMessageEntity::getRecordDate, date));
        return ConvertUtils.sourceToTarget(experimentMessageEntity, ExperimentMessageDto.class);
    }

    @Override
    public ExperimentMessageDto getExperimentMessageDto(Long groupId, LocalDate currentDay) {
        //查找是否已经存在记录
        LambdaQueryWrapper<ExperimentMessageEntity> countWrapper = Wrappers.<ExperimentMessageEntity>lambdaQuery()
                .eq(ExperimentMessageEntity::getGroupId, groupId)
                .eq(ExperimentMessageEntity::getRecordDate, currentDay);
        Long haveCount = experimentMessageDao.selectCount(countWrapper);
        if (haveCount > 0) {
            // 删除已经存在的记录
            experimentMessageDao.delete(countWrapper);
        }
        //统计当前实验组下昨天的情况
        ExperimentMessageDto experimentMessageDto = new ExperimentMessageDto();
        Long finishedNumber = groupsPersonDao.getFinishedNumber(groupId, currentDay);
        Long sumNumber = groupsPersonDao.getSumNumber(groupId, currentDay);
        // 统计一下进行中的人数  有进行时间 但是没有完成时间 或者 完成时间小于今天零点 那就是截止今天为止 未完成的人数
        Long processingNumber = groupsPersonDao.countProcessing(groupId, currentDay);
        experimentMessageDto.setGroupId(groupId);
        experimentMessageDto.setFinishedNumber(finishedNumber);
        experimentMessageDto.setProcessingNumber(processingNumber);
        experimentMessageDto.setSumNumber(sumNumber);
        experimentMessageDto.setRecordDate(currentDay);
        return experimentMessageDto;
    }
}