package com.nbtech.ailab.external.facade;

import com.nbtech.ailab.biz.dao.QuestionStarDataDao;
import com.nbtech.ailab.biz.entity.QuestionStarDataEntity;
import com.nbtech.ailab.common.RedisHeadEnum;
import com.nbtech.ailab.facade.ModelFacade;
import com.nbtech.ailab.vo.RecordParamVo;
import com.nbtech.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncDataFacade {

    @Autowired
    private RedisQueueFacade redisQueueFacade;

    @Autowired
    private QuestionStarDataDao questionStarDataDao;

    @Autowired
    private ModelFacade modelFacade;

    /**
     * 处理队列中的数据
     *
     * @param queueName 队列名称
     * @param processor 数据处理函数
     */
    public void processQueueData(String queueName, Consumer<List<Object>> processor) {
        try {
            Long queueSize = redisQueueFacade.getKeySize(queueName);
            if (Optional.ofNullable(queueSize).orElse(0L) == 0) {
                return;
            }

            List<Object> dataList = redisQueueFacade.pop(queueName);
            if (dataList != null && !dataList.isEmpty()) {
                processor.accept(dataList);
                log.info("Successfully processed {} records from queue: {}", dataList.size(), queueName);
            }
        } catch (Exception e) {
            log.error("Failed to process queue data: {}, error: {}", queueName, e.getMessage(), e);
        }
    }

    /**
     * 定时任务，每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void scheduledProcess() {
        processQueueData(RedisHeadEnum.QUESTION_START_RECEPTION.getDesc(), this::saveQuestionStartData);

        processQueueData(RedisHeadEnum.SAVE_CHAT_HISTORY.getDesc(), this::saveChatHistoryData);
    }

    /**
     * 读取redis中数据 保存到mysql数据库中
     *
     * @param dataList 数据列表
     */
    private void saveQuestionStartData(List<Object> dataList) {
        for (Object data : dataList) {
            QuestionStarDataEntity questionStarDataEntity = ConvertUtils.sourceToTarget(data, QuestionStarDataEntity.class);
            questionStarDataDao.insert(questionStarDataEntity);
        }
    }

    /**
     * 读取redis中数据 保存到mysql数据库中
     *
     * @param dataList 数据列表
     */
    private void saveChatHistoryData(List<Object> dataList) {
        for (Object data : dataList) {
            RecordParamVo recordParamVo = ConvertUtils.sourceToTarget(data, RecordParamVo.class);
            modelFacade.saveHistoryRecord(recordParamVo);
        }
    }

}
