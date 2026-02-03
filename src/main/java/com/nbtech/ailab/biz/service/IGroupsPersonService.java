package com.nbtech.ailab.biz.service;


import com.nbtech.ailab.biz.dto.AddressTotalDto;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.dto.UserInfoDto;
import com.nbtech.ailab.biz.entity.GroupsPersonEntity;
import com.nbtech.ailab.vo.ExperimentTotalVo;
import com.nbtech.common.service.CrudService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 实验人群包
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-04-25
 */
public interface IGroupsPersonService extends CrudService<GroupsPersonEntity, GroupsPersonDto> {


    /**
     * 根据用户id 实验组名称 实验组id获取唯一的实验组人群包
     *
     * @return dto
     */
    GroupsPersonDto getOnly(Long userId, Long experimentId, Long groupId);

    List<AddressTotalDto> getAddressByGroupId(Long id);

    List<UserInfoDto> getUserInfo(Long experimentId, Long groupId);

    List<GroupsPersonDto> getByGroupId(Long groupsId);

    GroupsPersonDto getByUserId(Long userId);
}