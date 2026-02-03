package com.nbtech.ailab.biz.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    List<SysRoleDto> getSysUserRole(@Param("userId") Long userId);

    /**
     * 批量删除用户的角色信息
     */
    void deleteUserRole(List<Long> idList);

    /**
     * 手写分页查询
     * @param page 分页条件
     * @param dto 查询条件
     * @return
     */
    Page<SysUserDto> getPage(Page<String> page,@Param("dto") SysUserDto dto);


    /**
     * 用户查询详情
     * @param dto 只有用户id
     * @return
     */
    List<SysUserDto> getPage(@Param("dto") SysUserDto dto);

    /**
     * 查询出所有没有创建dify账号的用户
     * @return
     */
    List<SysUserEntity> getNonDifyAccount();

    /**
     * 统计所有的教师数量
     * @return
     */
    Integer getTeacherNumber();
}