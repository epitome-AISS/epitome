package com.nbtech.ailab.biz.service;


import com.nbtech.ailab.biz.dto.SysUserRoleDto;
import com.nbtech.ailab.biz.entity.SysUserRoleEntity;
import com.nbtech.common.service.CrudService;

import java.util.List;

/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
public interface ISysUserRoleService extends CrudService<SysUserRoleEntity, SysUserRoleDto> {

    void deleteByUserId(Long userId);

    void deleteByRoleId(Long roleId);

    List<Integer> getUserIds(Long roleId);

    Long getByUserId(Long userId);
}