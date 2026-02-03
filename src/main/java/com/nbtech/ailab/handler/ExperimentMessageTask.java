package com.nbtech.ailab.handler;


import com.nbtech.ailab.biz.dao.GroupsPersonDao;
import com.nbtech.ailab.biz.dto.ExperimentMessageDto;
import com.nbtech.ailab.biz.entity.ExperimentMessageEntity;
import com.nbtech.ailab.biz.service.IExperimentMessageService;
import com.nbtech.ailab.common.PlanStatusEnum;
import com.nbtech.common.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.ParseException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


/**
 * 实验组分析信息定时任务
 */
@Component
public class ExperimentMessageTask {
    @Autowired
    private IExperimentMessageService experimentMessageService;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    /**
     * 实验组分析任务 获取每个实验组每天的实验情况
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void MessageTask() throws InterruptedException, ParseException {
        //获取所有 进行中 的实验组id集合
        List<Long> groupIds = groupsPersonDao.getGroupsList(PlanStatusEnum.BEEND.getDesc());

        //创建一个实验组信息集合用于存放各个实验组最后的分析信息
        List<ExperimentMessageDto> experimentMessageList = new ArrayList<>();
        // 延时十秒 确保数据在第二天了
        Thread.sleep(10000);
        //遍历每个实验组的情况
        for (Long groupId : groupIds) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            // 统计昨天开始的实验个数
            Long startNum = groupsPersonDao.countGroupStart(groupId, yesterday);

            // 查询这个实验组昨天是否有人进行了实验
            if (startNum == 0) {
                continue;
            }
            // 生成一条新的实验结果记录
            ExperimentMessageDto messageDto =experimentMessageService.getExperimentMessageDto(groupId,yesterday);
            experimentMessageList.add(messageDto);
        }
        experimentMessageService.insertBatch(ConvertUtils.sourceToTarget(experimentMessageList, ExperimentMessageEntity.class));

    }


}
