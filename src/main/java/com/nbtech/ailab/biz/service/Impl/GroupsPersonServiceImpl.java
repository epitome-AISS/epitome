package com.nbtech.ailab.biz.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.GroupsPersonDao;
import com.nbtech.ailab.biz.dao.SysUserDao;
import com.nbtech.ailab.biz.dto.AddressTotalDto;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.dto.UserInfoDto;
import com.nbtech.ailab.biz.entity.ExperimentPlanEntity;
import com.nbtech.ailab.biz.entity.GroupsPersonEntity;
import com.nbtech.ailab.biz.entity.SysUserEntity;
import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.excel.*;
import com.nbtech.ailab.util.FileUtils;
import com.nbtech.ailab.vo.AesKeyVo;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.ExperimentTotalVo;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.common.utils.ConvertUtils;
import com.nbtech.common.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
@Service
public class GroupsPersonServiceImpl extends CrudServiceImpl<GroupsPersonDao, GroupsPersonEntity, GroupsPersonDto> implements IGroupsPersonService {


    @Override
    public QueryWrapper<GroupsPersonEntity> getWrapper(GroupsPersonDto dto) {

        QueryWrapper<GroupsPersonEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_date");
        return wrapper;
    }


    /**
     * 默认样式策略策略
     *
     * @return
     */
    private static HorizontalCellStyleStrategy defaultStylePolicyPolicy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 表头背景色为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置字体为微软雅黑
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置字体为微软雅黑
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

    }


    @Override
    public GroupsPersonDto getOnly(Long userId, Long experimentId, Long groupId) {
        GroupsPersonEntity groupsPersonEntity = this.baseDao.selectOne(Wrappers.<GroupsPersonEntity>lambdaQuery()
                .eq(GroupsPersonEntity::getUserId, userId)
                .eq(GroupsPersonEntity::getExperimentId, experimentId)
                .eq(GroupsPersonEntity::getGroupsId, groupId));
        return ConvertUtils.sourceToTarget(groupsPersonEntity, GroupsPersonDto.class);
    }


    /**
     * 根据实验组id获取到对应的地域分布集合
     *
     * @param id 实验组id
     * @return 集合
     */
    @Override
    public List<AddressTotalDto> getAddressByGroupId(Long id) {
        return this.baseDao.getAddressByGroupId(id);
    }

    /**
     * 根据实验计划id 实验组id 获取用户信息
     *
     * @param experimentId 实验计划id
     * @param groupId      实验组id
     * @return 用户信息
     */
    @Override
    public List<UserInfoDto> getUserInfo(Long experimentId, Long groupId) {
        return this.baseDao.getUserInfo(experimentId, groupId);
    }

    @Override
    public List<GroupsPersonDto> getByGroupId(Long groupsId) {
        List<GroupsPersonEntity> groupsPersonEntities = this.baseDao.selectList(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getGroupsId, groupsId));
        return ConvertUtils.sourceToTarget(groupsPersonEntities, GroupsPersonDto.class);
    }

    @Override
    public GroupsPersonDto getByUserId(Long userId) {
        GroupsPersonEntity groupsPersonEntity = this.baseDao.selectOne(
                Wrappers.<GroupsPersonEntity>lambdaQuery()
                        .eq(GroupsPersonEntity::getUserId, userId));
        return ConvertUtils.sourceToTarget(groupsPersonEntity, GroupsPersonDto.class);
    }

}