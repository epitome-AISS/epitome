package com.nbtech.ailab.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.util.ShiroUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动给每个记录添加制单人制单日期
 */
@Component
public class DataMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTimeUtil.now();
        Long userId;
        String userName;
        try {
            SysUserDto sysUserDto = ShiroUtils.getUserEntity();
            userId = sysUserDto.getId();
            userName = sysUserDto.getUsername();
        } catch (Exception e) {  //不是用户线程，可能是内部线程池在进行操作，此时获取不到用户id
            userId = 0L;
            userName = "系统管理员";
        }

        metaObject.setValue("creator", userId);
        metaObject.setValue("createName", userName);
        metaObject.setValue("createDate", now);
        metaObject.setValue("updater", userId);
        metaObject.setValue("updateName", userName);
        metaObject.setValue("updateDate", now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTimeUtil.now();
        Long userId;
        String userName;
        try {
            SysUserDto sysUserDto = ShiroUtils.getUserEntity();
            userId = sysUserDto.getId();
            userName = sysUserDto.getUsername();
        } catch (Exception e) {  //不是用户线程，可能是内部线程池在进行操作，此时获取不到用户id
            userId = 0L;
            userName = "系统管理员";
        }
        metaObject.setValue("updater", userId);
        metaObject.setValue("updateName", userName);
        metaObject.setValue("updateDate", now);
    }
}
