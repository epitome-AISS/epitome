package com.nbtech.ailab.biz.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.vo.SysRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    /**
     * 根据用户权限获取角色
     * @param userId 用户Id
     * @return
     */
    SysRoleEntity getRoleByUserId(Long userId);

    /**
     * 获取用户所有角色
     * @param userId 用户Id
     * @return
     */
    List<SysRoleEntity> getRole(Long userId);

    /**
     * 查询所有的角色
     * @param roleName 角色名称
     * @return
     */
    List<SysRoleVo> getRoleList(String roleName);
}