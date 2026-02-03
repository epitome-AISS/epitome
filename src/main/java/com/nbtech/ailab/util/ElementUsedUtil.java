package com.nbtech.ailab.util;

import com.nbtech.ailab.biz.dao.ExperimentPlanDao;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dto.ParamDto;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ElementUsedUtil {
    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ExperimentPlanDao experimentPlanDao;

    /**
     * 根据算子类型和算子本身的id 校验是否能被删除
     *
     * @param type     type
     * @param commonId commonId
     */
    public void validatePublishElement(String type, Long commonId, String username) {
        //判断当前是不是本人在修改 防止其他人恶意修改
        // 查询所有的实验组引用的算子
        List<ParamDto> commonParams = getParamList(type);

        //以实验计划id为键
        Map<Long, Set<ParamDto>> paramMap = commonParams
                .stream()
                .collect(Collectors.groupingBy(ParamDto::getExperimentId, Collectors.toSet()));

        //以算子id为键
        Map<Long, Set<ParamDto>> paramMap2 = commonParams
                .stream()
                .collect(Collectors.groupingBy(ParamDto::getElementId, Collectors.toSet()));

        //获取所有实验计划id
        Set<Long> experimentIds = paramMap.keySet();

        //获取发布后的实验计划id
        List<Long> publishIds = experimentPlanDao.getProcessingPlanIds();

        List<Long> finalIds = new ArrayList<>();

        // 如果这个模型没有没引用 那就不用查询是否被引用
        if (!paramMap2.containsKey(commonId)) {
            return;
        }
        //收集当前实验算子id下所有的创建者
        List<String> finalNames = paramMap2.get(commonId).stream().map(ParamDto::getCreateName).distinct().collect(Collectors.toList());

//        Map<Long, String> map = new HashMap<>();

        //对所有实验计划id进行遍历 获取发布后的实验计划id
        for (Long experimentId : experimentIds) {
            //如果发布后的实验计划不包含当前的实验计划id 跳出循环
            if (!publishIds.contains(experimentId)) {
                continue;
            }
            finalIds.addAll(paramMap.get(experimentId).stream().map(ParamDto::getElementId).collect(Collectors.toList()));
//            map.put(experimentId, paramMap.get(experimentId).stream().map(ParamDto::getCreateName).findFirst().get());
        }

        //获取到的是最终发布的算子id集合
        List<Long> distinctIds = finalIds.stream().distinct().collect(Collectors.toList());
        for (Long id : distinctIds) {
            if (commonId.equals(id)) {
                //我当前算子被多个实验组创建者引用 不是仅被自己引用
                if ((!finalNames.isEmpty() && finalNames.size() > 1) || (finalNames.size() == 1 && !finalNames.get(0).equals(username))) {
                    throw new BizException(BizResponseCodeEnum.ELEMENT_NOT_ONLY_USED_BY_OWN);
                }
                throw new BizException(BizResponseCodeEnum.CURRENT_ELEMENT_HAVE_BEEN_USED);
            }
        }
    }

    /**
     * 获取未发布的算子id集合
     */
    public List<Long> getNotPublishElementIds(String type) {
        List<ParamDto> commonParams = getParamList(type);

        //以实验计划id为键
        Map<Long, Set<ParamDto>> paramMap = commonParams
                .stream()
                .collect(Collectors.groupingBy(ParamDto::getExperimentId, Collectors.toSet()));

        //获取所有实验计划id
        Set<Long> experimentIds = paramMap.keySet();

        //获取发布后的实验计划id
        List<Long> publishIds = experimentPlanDao.getProcessingPlanIds();

        List<Long> finalIds = new ArrayList<>();

        //对所有实验计划id进行遍历 获取未发布后的实验计划id
        for (Long experimentId : experimentIds) {
            //如果当前实验计划发布 跳出当前循环 进行下一次循环
            if (publishIds.contains(experimentId)) {
                continue;
            }
            finalIds.addAll(paramMap.get(experimentId).stream().map(ParamDto::getElementId).collect(Collectors.toList()));
        }
        return finalIds.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取实验组引用算子的对象集合
     *
     * @param type type
     * @return 对象集合
     */
    public List<ParamDto> getParamList(String type) {
        List<ParamDto> commonParams = new ArrayList<>();
        switch (type) {
            case "collection":
                commonParams = groupsDao.getGroupsHaveQuestionnaireIds();
                break;
            case "dialogue":
                commonParams = groupsDao.getGroupsHaveDialogueIds();
                break;
            case "intervene":
                commonParams = groupsDao.getGroupsHaveInterveneIds();
                break;
            default:
                break;
        }
        return commonParams;
    }
}
