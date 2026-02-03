package com.nbtech.ailab.security;


import com.nbtech.ailab.biz.dao.SysRoleDao;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.common.AuthRoleEnum;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.UserConstant;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证
 */
@Component
public class Oauth2Realm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysRoleDao sysRoleDao;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserDto user = (SysUserDto) principals.getPrimaryPrincipal();
        // 查询用户所有角色集合
        List<SysRoleEntity> roleEntityList = sysRoleDao.getRole(user.getId());
        Set<Long> roleSet = roleEntityList.stream().map(SysRoleEntity::getId).collect(Collectors.toSet());
        // 鉴权使用的角色列表
        Set<String> newRole = new HashSet<>();
        if (!roleSet.isEmpty() && roleSet.contains(3L)){
            newRole.add(AuthRoleEnum.EXPERIMENTER.getDesc());
        }else {
            newRole.add(AuthRoleEnum.MANAGER.getDesc());
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(newRole);
        return info;
    }



    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = ((UsernamePasswordToken) token).getUsername();
        // 查询用户信息
        SysUserDto userEntity = sysUserService.getByUsername(username);
        // 账号不存在
        if (userEntity == null) {
            throw new UnknownAccountException("账号不存在");
        }
        // 账号锁定
        if (userEntity.getStatus() == 0) {
            throw new LockedAccountException(BizResponseCodeEnum.USER_FROZEN.getMessage());
        }
        // 校验密码
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userEntity, userEntity.getPassword(), getName());
        info.setCredentialsSalt(ByteSource.Util.bytes((UserConstant.SALT_PREFIX + username).getBytes()));
        return info;
    }

    public Oauth2Realm(@Autowired HashedCredentialsMatcher hashedCredentialsMatcher) {
        super.setCredentialsMatcher(hashedCredentialsMatcher);
    }



}