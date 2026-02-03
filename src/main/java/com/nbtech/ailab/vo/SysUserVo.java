package com.nbtech.ailab.vo;



import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author van
 */
@Data
@Accessors(chain = true)
public class SysUserVo extends SysUserDto {

//    /**
//     * 角色
//     */
//    private List<SysRoleDto> roles;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限
     */
    private String permissions;

    /**
     * 消息权限
     */
    private String messagePermissions;

    /**
     * 是否为数字孪生用户
     */
    private boolean dtUser;
}
