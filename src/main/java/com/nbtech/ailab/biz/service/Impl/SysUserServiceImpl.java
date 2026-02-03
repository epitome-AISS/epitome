package com.nbtech.ailab.biz.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nbtech.ailab.biz.dao.SysRoleDao;
import com.nbtech.ailab.biz.entity.SysRoleEntity;
import com.nbtech.ailab.common.UserStatusEnum;
import com.nbtech.ailab.external.vo.DifyLoginVo;
import com.nbtech.ailab.external.vo.RegisterUserVo;
import com.nbtech.ailab.util.*;
import com.nbtech.ailab.vo.AuthenticationParamVo;
import com.nbtech.ailab.vo.ExperimentVo;
import com.nbtech.common.utils.ConvertUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.SysUserDao;
import com.nbtech.ailab.biz.dto.SysRoleDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.dto.SysUserRoleDto;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import com.nbtech.ailab.biz.service.ISysUserRoleService;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.constant.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-22
 */
@Service
public class SysUserServiceImpl extends CrudServiceImpl<SysUserDao, SysUserEntity, SysUserDto> implements ISysUserService {

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    public QueryWrapper<SysUserEntity> getWrapper(SysUserDto dto) {
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.like(BlankStringUtil.isBlank(dto.getUsername()), "username", dto.getUsername());
        wrapper.like(BlankStringUtil.isBlank(dto.getRealName()), "real_name", dto.getRealName());
        wrapper.like(BlankStringUtil.isBlank(dto.getEmail()), "email", dto.getEmail());
        wrapper.like(BlankStringUtil.isBlank(dto.getMobile()), "mobile", dto.getMobile());
        wrapper.like("status", 1);
        wrapper.orderByDesc("update_date");
        return wrapper;
    }

    @Override
    public SysUserDto getByUsername(String username) {
        SysUserEntity entity = this.baseDao.selectOne(Wrappers.<SysUserEntity>lambdaQuery().eq(SysUserEntity::getUsername, username));
        return ConvertUtils.sourceToTarget(entity, SysUserDto.class);
    }

    @Override
    public List<SysRoleDto> getRoleById(Long userId) {
        return baseDao.getSysUserRole(userId);
    }

    @Override
    public void activeBatch(Long[] ids) {
        Long userId = ShiroUtils.getUserId();
        this.update(SysUserEntity.builder()
                        .status(1)
                        .updater(userId)
                        .updateDate(LocalDateTime.now())
                        .build(),
                Wrappers.<SysUserEntity>lambdaUpdate()
                        .in(SysUserEntity::getId, Arrays.asList(ids)));
    }

    @Override
    public void inactiveBatch(Long[] ids) {
        Long userId = ShiroUtils.getUserId();
        this.update(SysUserEntity.builder()
                        .status(0)
                        .updater(userId)
                        .updateDate(LocalDateTime.now())
                        .build(),
                Wrappers.<SysUserEntity>lambdaUpdate()
                        .in(SysUserEntity::getId, Arrays.asList(ids)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserVo vo) {
        Long userId = ShiroUtils.getUserId();
        String username = vo.getUsername();
        SysUserDto userDto = getByUsername(username);
        if (userDto != null) {
            throw new BizException(BizResponseCodeEnum.USERNAME_REPEAT);
        }
        // 密码sha256加密
        String password = generatePassword(username, vo.getPassword());
        vo.setPassword(password);
        vo.setCreator(userId);
        vo.setCreateDate(LocalDateTime.now());
        vo.setStatus(1);
        vo.setUpdater(userId);
        vo.setUpdateDate(LocalDateTime.now());

        save(vo);
        SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
        sysUserRoleDto.setUserId(vo.getId().intValue());
        sysUserRoleDto.setRoleId(vo.getRoleId());

        // 创建dify的用户
        SysUserEntity sysUserEntity = ConvertUtils.sourceToTarget(vo, SysUserEntity.class);
        // 每个新建的用户配置基础模型
        // 新增ailab用户
        userRoleService.save(sysUserRoleDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(SysUserVo vo) {
        Long userId = ShiroUtils.getUserId();
        String username = vo.getUsername();
        SysUserDto userDto = getByUsername(username);
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
        update(vo);
        userRoleService.deleteByUserId(vo.getId());
        SysUserRoleDto sysUserRoleDto = new SysUserRoleDto();
        sysUserRoleDto.setUserId(vo.getId().intValue());
        sysUserRoleDto.setRoleId(vo.getRoleId());
        userRoleService.save(sysUserRoleDto);
    }

    /**
     * 批量删除用户和用户的角色
     *
     * @param idList 用户id集合
     */
    @Override
    @Transactional
    public void deleteUser(List<Long> idList) {
        delete(idList);
        // 删除用户角色记录
        baseDao.deleteUserRole(idList);
    }

    @Override
    public PageResult<SysUserDto> getPage(PageDto pageDto, SysUserDto dto) {
        Page<String> page = new Page<>(pageDto.getCurrent(), pageDto.getSize());
        Page<SysUserDto> ipage = baseDao.getPage(page, dto);
        List<SysUserDto> targetList = ConvertUtils.sourceToTarget(ipage.getRecords(), this.currentDtoClass());
        return PageResult.build(ipage, targetList);
    }

    @Override
    public SysUserDto getInfo(Long id) {
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setId(id);
        List<SysUserDto> sysUserDtoList = baseDao.getPage(sysUserDto);
        if (sysUserDtoList.isEmpty()) {
            return null;
        }
        SysUserDto result = sysUserDtoList.get(0);
        return result;
    }

    @Override
    public SysUserDto getUserInfo() {
        SysUserDto sysUserDto = ShiroUtils.getUserEntity();
        if (!Optional.ofNullable(sysUserDto).isPresent()) {
            throw new BizException(BizResponseCodeEnum.SESSION_TIMEOUT);
        }
        // 获取用户的角色信息
        List<SysRoleEntity> roleEntities = sysRoleDao.getRole(sysUserDto.getId());
        sysUserDto.setRoleIds(String.valueOf(roleEntities.get(0).getId()));
        sysUserDto.setRoleName(roleEntities.get(0).getName());
        if (roleEntities.get(0).getId() == 3L) {
            // 受试者返回实验信息
            List<ExperimentVo> experiment = groupsDao.getExperiment(sysUserDto.getId());
            List<Long> experimentId = new ArrayList<>();
            List<Long> groupId = new ArrayList<>();
            for (ExperimentVo experimentVo : experiment) {
                experimentId.add(experimentVo.getExperimentIds());
                groupId.add(experimentVo.getGroupIds());
            }
            sysUserDto.setExperimentIds(experimentId);
            sysUserDto.setGroupIds(groupId);
        }
        return sysUserDto;
    }

    @Override
    public void forbidUser(Long id, Boolean status) {
        int state;
        if (status) {
            // 启用
            state = UserStatusEnum.ENABLE.getDesc();
        } else {
            // 禁用 并下线
            state = UserStatusEnum.FORBID.getDesc();
//            ShiroUtils.kickOut(id);
        }
        baseDao.update(null, Wrappers.<SysUserEntity>lambdaUpdate()
                .eq(SysUserEntity::getId, id)
                .set(SysUserEntity::getStatus, state));
    }


    private String generatePassword(String username, String password) {
        return ShiroUtils.sha256WithSalt(password, UserConstant.SALT_PREFIX + username);
    }

    @Override
    public Integer getTeacherNumber() {
        return baseDao.getTeacherNumber();
    }

    @Override
    public SysUserEntity authenUser(AuthenticationParamVo paramVo) {
        return baseDao.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(paramVo.getRealName() != null, SysUserEntity::getRealName, paramVo.getRealName())
                .eq(paramVo.getEmail() != null, SysUserEntity::getEmail, paramVo.getEmail())
                .eq(paramVo.getMobile() != null, SysUserEntity::getMobile, paramVo.getMobile())
                .last("limit 1"));
    }

}