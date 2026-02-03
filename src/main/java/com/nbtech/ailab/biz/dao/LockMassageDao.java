package com.nbtech.ailab.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nbtech.ailab.biz.dto.LockMassageDto;
import com.nbtech.ailab.biz.entity.LockMassageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 锁住记录
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-12-27
 */
@Mapper
public interface LockMassageDao extends BaseMapper<LockMassageEntity> {

    /**
     * 获取最新的封锁状态
     *
     * @param lockMassageDto 查询条件
     * @return
     */
    List<LockMassageEntity> getNewLockMassage(LockMassageDto lockMassageDto, Boolean previewStatus);

    /**
     * 解开所有因它而被锁住的记录
     *
     * @param lockMassageDto 封锁状态记录
     * @return
     */
    void unlockByWork(LockMassageDto lockMassageDto, String workId, Boolean previewStatus);

    /**
     * 获取哪些任务因为它而被锁住
     */
    List<LockMassageEntity> getLockMassagesByWorkId(String lockReason, Boolean previewStatus);
}