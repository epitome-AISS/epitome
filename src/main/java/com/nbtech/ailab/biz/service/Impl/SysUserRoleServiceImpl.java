package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.SysUserRoleDao;
import com.nbtech.ailab.biz.dto.SysUserRoleDto;
import com.nbtech.ailab.biz.entity.SysUserRoleEntity;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Service
public class SysUserRoleServiceImpl extends CrudServiceImpl<SysUserRoleDao, SysUserRoleEntity, SysUserRoleDto> implements ISysUserRoleService {

    @Override
    public QueryWrapper<SysUserRoleEntity> getWrapper(SysUserRoleDto dto) {

        QueryWrapper<SysUserRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(dto.getUserId() != null, "userId", dto.getUserId());
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public void deleteByUserId(Long userId) {
        this.baseDao.delete(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId, userId));
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        this.baseDao.delete(Wrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getRoleId, roleId)
        );
    }

    @Override
    public List<Integer> getUserIds(Long roleId) {
        List<SysUserRoleEntity> userList = this.baseDao.selectList(Wrappers.<SysUserRoleEntity>query()
                .select("distinct user_id userId")
                .eq("role_id", roleId)
        );
        List<Integer> userIds = CollectionUtils.isEmpty(userList) ? null : userList.stream().map(SysUserRoleEntity::getUserId).collect(Collectors.toList());
        return userIds;
    }

    /**
     * 根据用户id获取角色id
     *
     * @param userId userId
     * @return 角色id
     */
    @Override
    public Long getByUserId(Long userId) {
        SysUserRoleEntity sysUserRoleEntity = this.baseDao.selectOne(
                Wrappers.<SysUserRoleEntity>lambdaQuery()
                        .eq(SysUserRoleEntity::getUserId, userId));
        Long roleId = sysUserRoleEntity.getRoleId();
        return roleId;
    }

}