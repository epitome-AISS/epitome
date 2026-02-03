package com.nbtech.ailab.biz.service;



import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.vo.SysRoleVo;
import com.nbtech.common.service.CrudService;

import java.util.List;
import java.util.Set;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
public interface ISysRoleService extends CrudService<SysRoleEntity, SysRoleDto> {

    /**
     * 根据用户权限获取角色
     * @param userId 用户Id
     * @return
     */
    SysRoleEntity getRoleByUserId(Long userId);

    /**
     * 查询所有角色
     * @param roleName 角色名称
     * @return
     */
    List<SysRoleVo> getRoleList(String roleName);
}