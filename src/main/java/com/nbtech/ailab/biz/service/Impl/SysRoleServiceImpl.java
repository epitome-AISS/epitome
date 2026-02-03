package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.nbtech.ailab.biz.dao.SysRoleDao;
import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.biz.service.ISysRoleService;
import com.nbtech.ailab.vo.SysRoleVo;
import com.nbtech.common.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Service
public class SysRoleServiceImpl extends CrudServiceImpl<SysRoleDao, SysRoleEntity, SysRoleDto> implements ISysRoleService {

    @Override
    public QueryWrapper<SysRoleEntity> getWrapper(SysRoleDto dto){

        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public SysRoleEntity getRoleByUserId(Long userId) {
        return baseDao.getRoleByUserId(userId);
    }

    @Override
    public List<SysRoleVo> getRoleList(String roleName) {
        return baseDao.getRoleList(roleName);
    }
}