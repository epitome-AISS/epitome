package com.nbtech.ailab.facade;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.dto.SysUserRoleDto;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import com.nbtech.ailab.biz.entity.SysUserRoleEntity;
import com.nbtech.ailab.biz.service.ISysRoleService;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.UserConstant;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class UserFacade {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserRoleService userRoleService;

    public PageResult<SysUserVo> page(PageDto pageDto, SysUserDto dto) {
        PageResult<SysUserDto> page = sysUserService.page(pageDto, dto);
        List<SysUserVo> list = new ArrayList<>();
        for (SysUserDto userDto : page.getRecords()) {
            SysUserVo vo = BeanUtil.copyProperties(userDto, SysUserVo.class);

            list.add(vo);
        }
        PageResult<SysUserVo> result = new PageResult<>();
        result.setRecords(list);
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        return result;
    }

    public SysUserVo get(Long id) {
        SysUserDto userDto = sysUserService.get(id);
        SysUserVo vo = BeanUtil.copyProperties(userDto, SysUserVo.class);
        List<SysRoleDto> roles = sysUserService.getRoleById(vo.getId());
        sysUserService.getByUsername(null);
        // 添加角色id 暂定为1个
        vo.setRoleId(roles.get(0).getId());

        Set<String> set = new HashSet<>();
        Set<String> messageSet = new HashSet<>();
        boolean dtUser = false;
        for (SysRoleDto roleDto : roles) {
            SysRoleDto roleDtoDetail = sysRoleService.get(roleDto.getId().longValue());
            if (StringUtils.isNotBlank(roleDtoDetail.getPermissions())) {
                List<String> items = new ArrayList<>(JSON.parseArray(roleDtoDetail.getPermissions(), String.class));
                set.addAll(items);
            }
            if (StringUtils.isNotBlank(roleDtoDetail.getHalfCheckedKeys())) {
                List<String> items = new ArrayList<>(JSON.parseArray(roleDtoDetail.getHalfCheckedKeys(), String.class));
                set.addAll(items);
            }
            if (StringUtils.isNotBlank(roleDtoDetail.getMessagePermissions())) {
                List<String> items = new ArrayList<>(JSON.parseArray(roleDtoDetail.getMessagePermissions(), String.class));
                messageSet.addAll(items);
            }
            if (StringUtils.isNotBlank(roleDtoDetail.getMessageHalfCheckedKeys())) {
                List<String> items = new ArrayList<>(JSON.parseArray(roleDtoDetail.getMessageHalfCheckedKeys(), String.class));
                messageSet.addAll(items);
            }
        }
        vo.setDtUser(dtUser);
        if (CollectionUtil.isNotEmpty(set)) {
            vo.setPermissions(JSON.toJSONString(set));
        }
        if (CollectionUtil.isNotEmpty(messageSet)) {
            vo.setMessagePermissions(JSON.toJSONString(messageSet));
        }
        return vo;
    }

    public String getUserRealName(Long id) {
        SysUserDto userDto = sysUserService.get(id);
        return userDto.getRealName();
    }

    public Collection<String> getMessagePermissions(Long userId) {
        List<SysRoleDto> roles = sysUserService.getRoleById(userId);
        Set<String> messageSet = new HashSet<>();
        for (SysRoleDto roleDto : roles) {
            SysRoleDto roleDtoDetail = sysRoleService.get(roleDto.getId().longValue());
            if (StringUtils.isNotBlank(roleDtoDetail.getMessagePermissions())) {
                List<String> items = new ArrayList<>(JSON.parseArray(roleDtoDetail.getMessagePermissions(), String.class));
                messageSet.addAll(items);
            }
        }
        return messageSet;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserVo vo) {
        Long userId = ShiroUtils.getUserId();
        String username = vo.getUsername();
        SysUserDto userDto = sysUserService.getByUsername(username);
        if (userDto != null) {
            throw new BizException(BizResponseCodeEnum.USERNAME_REPEAT);
        }
        // 密码sha256加密
        String password = generatePassword(username, vo.getPassword());
        vo.setPassword(password);
        vo.setCreator(userId);
        vo.setCreateDate(LocalDateTime.now());
        vo.setUpdater(userId);
        vo.setUpdateDate(LocalDateTime.now());
        sysUserService.save(vo);
        SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
        sysUserRoleDto.setUserId(vo.getId().intValue());
        sysUserRoleDto.setRoleId(vo.getRoleId());
        userRoleService.save(sysUserRoleDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SysUserEntity> batchSave(List<SysUserVo> sysUserVos) {
        List<SysUserEntity> sysUserEntities = new ArrayList<>();
        List<SysUserRoleEntity> SysUserRoleEntities = new ArrayList<>();
        for (SysUserVo vo : sysUserVos) {
            Long userId = ShiroUtils.getUserId();
            String username = vo.getUsername();
            String originlPassword = vo.getPassword();
            // 密码sha256加密
            String password = generatePassword(username, originlPassword);
            vo.setPassword(password);
            vo.setCreator(userId);
            vo.setCreateDate(LocalDateTime.now());
            vo.setUpdater(userId);
            vo.setUpdateDate(LocalDateTime.now());
            SysUserEntity sysUserEntity = ConvertUtils.sourceToTarget(vo, SysUserEntity.class);
            sysUserEntity.setOriginPassword(originlPassword);
            sysUserEntities.add(sysUserEntity);

        }
        if (CollectionUtil.isNotEmpty(sysUserEntities)) {
            sysUserService.insertBatch(sysUserEntities, 1000);
        }
        for (SysUserEntity userDto : sysUserEntities) {
            SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
            sysUserRoleDto.setUserId(userDto.getId().intValue());
            sysUserRoleDto.setRoleId(3L);
            SysUserRoleEntities.add(ConvertUtils.sourceToTarget(sysUserRoleDto, SysUserRoleEntity.class));
        }
        if (CollectionUtil.isNotEmpty(SysUserRoleEntities)) {
            userRoleService.insertBatch(SysUserRoleEntities, 1000);
        }
        return sysUserEntities;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysUserEntity saveOneUser(SysUserVo vo) {

        String username = vo.getUsername();
        String originalPassword = vo.getPassword();
        // 密码sha256加密
        String password = generatePassword(username, originalPassword);
        vo.setPassword(password);
        vo.setCreator(1L);
        vo.setCreateDate(LocalDateTime.now());
        vo.setUpdater(1L);
        vo.setUpdateDate(LocalDateTime.now());
        SysUserEntity sysUserEntity = ConvertUtils.sourceToTarget(vo, SysUserEntity.class);
        sysUserService.saveUser(vo);
        sysUserEntity.setOriginPassword(originalPassword);

        SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
        sysUserRoleDto.setUserId(vo.getId().intValue());
        sysUserRoleDto.setRoleId(3L);
        userRoleService.save(sysUserRoleDto);
        sysUserEntity.setId(vo.getId());

        return sysUserEntity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserVo vo) {
        Long userId = ShiroUtils.getUserId();
        String username = vo.getUsername();
        SysUserDto userDto = sysUserService.getByUsername(username);
        if (userDto != null && !userDto.getId().equals(vo.getId())) {
            throw new BizException(BizResponseCodeEnum.USERNAME_REPEAT);
        }
        // 密码sha256加密
        if (vo.getPassword() != null) {
            String password = generatePassword(username, vo.getPassword());
            vo.setPassword(password);
        }
        vo.setUpdater(userId);
        vo.setUpdateDate(LocalDateTime.now());
        sysUserService.update(vo);


        userRoleService.deleteByUserId(vo.getId());
        SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
        sysUserRoleDto.setUserId(vo.getId().intValue());
        sysUserRoleDto.setRoleId(vo.getRoleId());
        userRoleService.save(sysUserRoleDto);
        ShiroUtils.kickOut(vo.getId());

    }

    public void activeBatch(Long[] ids) {
        sysUserService.activeBatch(ids);
    }

    public void inactiveBatch(Long[] ids) {
        sysUserService.inactiveBatch(ids);
    }

    public void updateRole(SysRoleDto dto) {
        List<Integer> userIds = userRoleService.getUserIds(dto.getId());
        SysRoleDto previous = sysRoleService.get(dto.getId().longValue());
        boolean kickOutUsers = false;
        if (previous.getHalfCheckedKeys() != null && !previous.getHalfCheckedKeys().equals(dto.getHalfCheckedKeys())) {
            kickOutUsers = true;
        }
        if (previous.getMessageHalfCheckedKeys() != null && !previous.getMessageHalfCheckedKeys().equals(dto.getMessageHalfCheckedKeys())) {
            kickOutUsers = true;
        }
        if (previous.getPermissions() != null && !previous.getPermissions().equals(dto.getPermissions())) {
            kickOutUsers = true;
        }
        if (previous.getMessagePermissions() != null && !previous.getMessagePermissions().equals(dto.getMessagePermissions())) {
            kickOutUsers = true;
        }
        sysRoleService.update(dto);
        if (kickOutUsers && CollectionUtils.isNotEmpty(userIds)) {
            ShiroUtils.kickOut(userIds);
        }
    }

    public void deleteRole(Long roleId) {
        List<Integer> userIds = userRoleService.getUserIds(roleId);
        sysRoleService.delete(new ArrayList<Long>() {{
            add(roleId);
        }});
        userRoleService.deleteByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(userIds)) {
            ShiroUtils.kickOut(userIds);
        }
    }


    private String generatePassword(String username, String password) {
        return ShiroUtils.sha256WithSalt(password, UserConstant.SALT_PREFIX + username);
    }

}
