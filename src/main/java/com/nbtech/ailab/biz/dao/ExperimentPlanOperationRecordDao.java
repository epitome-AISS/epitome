package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.entity.ExperimentPlanOperationRecordEntity;
import com.nbtech.ailab.vo.OperationRecordVo;
import com.nbtech.ailab.vo.RecordStatusVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 实验更新表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Mapper
public interface ExperimentPlanOperationRecordDao extends BaseMapper<ExperimentPlanOperationRecordEntity> {

    /**
     * 查询这个实验下的各个状态的结果
     */
    List<OperationRecordVo> getOperation(RecordStatusVo recordStatusVo);
}