package com.nbtech.ailab.biz.service;


import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import com.nbtech.ailab.external.vo.DifyLoginVo;
import com.nbtech.ailab.vo.AuthenticationParamVo;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.CrudService;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
public interface ISysUserService extends CrudService<SysUserEntity, SysUserDto> {

    SysUserDto getByUsername(String username);

    List<SysRoleDto> getRoleById(Long userId);

    void activeBatch(Long[] ids);

    void inactiveBatch(Long[] ids);

    /**
     * 保存用户
     */
    void saveUser(SysUserVo sysUserVo);

    /**
     * 编辑用户
     */
    void updateUser(SysUserVo sysUserVo);

    /**
     * 删除用户
     */
    void deleteUser(List<Long> idList);

    /**
     * 用户分页查询
     * @param pageDto
     * @param dto
     * @return
     */
    PageResult<SysUserDto> getPage(PageDto pageDto, SysUserDto dto);

    /**
     * 用户查询详情
     * @param id 用户id
     * @return
     */
    SysUserDto getInfo(Long id);

    /**
     * 根据当前用户session获取用户信息
     */
    SysUserDto getUserInfo();

    /**
     * 用户启用和禁用
     * @param id 用户id
     * @param status true起用 false 禁用
     */
    void forbidUser(Long id,Boolean status);


    /**
     * 统计所有的教师数量
     * @return
     */
    Integer getTeacherNumber();

    /**
     * 验证身份信息
     * @param paramVo
     * @return
     */
    SysUserEntity authenUser(AuthenticationParamVo paramVo);
}