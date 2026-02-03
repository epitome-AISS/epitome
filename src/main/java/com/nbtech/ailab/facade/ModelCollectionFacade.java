package com.nbtech.ailab.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.BasicModelDao;
import com.nbtech.ailab.biz.dao.ModelDao;
import com.nbtech.ailab.biz.dao.ModelHistoryDao;
import com.nbtech.ailab.biz.dto.ModelDto;
import com.nbtech.ailab.biz.entity.BasicModelEntity;
import com.nbtech.ailab.biz.entity.ModelEntity;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.biz.service.IModelService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.constant.GraphConstant;
import com.nbtech.ailab.util.CodeUtil;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.exception.BizException;
import org.ehcache.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nber
 */
@Component
public class ModelCollectionFacade {

    private static final Logger log = LoggerFactory.getLogger(ModelCollectionFacade.class);
    @Autowired
    private IModelService modelService;

    @Autowired
    private ModelHistoryDao modelHistoryDao;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private BasicModelDao basicModelDao;


    /**
     * 查询某个实验组下的所有模型的算子集合
     *
     * @param groupId 实验组id
     */
    public List<ModelDto> getModelElement(Long groupId) {
        // 查询实验组下所有算子
        List<ElementVo> elementVos = groupsService.getElementVo(groupId);
        ObjectMapper objectMapper = new ObjectMapper();

        List<ElementVo> elementVoList = elementVos.stream()
                .filter(elementVo -> ElementTypeEnum.MODEL.getDesc().equals(elementVo.getType()))
                .collect(Collectors.toList());
        List<ModelDto> modelDtoList = new ArrayList<>();
        int i = 0;
        for (ElementVo element : elementVoList) {
            ModelJsonVo modelJsonVo = objectMapper.convertValue(element.getConfig(), ModelJsonVo.class);
            ModelDto dto = modelService.get(Long.valueOf(modelJsonVo.getDialogueId()));
            if (dto.getModels() == null || dto.getModels().isEmpty()) {
                continue;
            }
            // 算子的id一起返回给前端
            dto.setElementId(element.getId());
            dto.setModelName(dto.getModelName() + "_" + (element.getSequence() + 1L));
            // 设置一个唯一键 保证模型重复也不会有重复的键
            dto.setSoleKey(i);
            modelDtoList.add(dto);
            ++i;
        }
        return modelDtoList;
    }

    /**
     * 获取某个大模型下的所有基础模型
     *
     * @param modelId 模型id
     */
    public List<ModelConfigVo> getModelList(Long modelId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 获取模型
        ModelDto modelDto = modelService.get(modelId);
        if (!Optional.ofNullable(modelDto).isPresent()) {
            log.info("获取大模型下所有基础模型的时候 大的模型不存在 大的模型的id是 {}", modelId);
            throw new BizException(BizResponseCodeEnum.EXISTS_NOT_MODEL);
        }
        // 转json配置
        return objectMapper.readValue(modelDto.getModels(), objectMapper.getTypeFactory().constructCollectionType(List.class, ModelConfigVo.class));
    }

    /**
     * 统计这个算子下模型的使用人数
     *
     * @param paramVo 选中的模型
     */
    public List<ModelUserVo> getModelUser(EleParamVo paramVo) throws JsonProcessingException {
        // 选中的模型名称为空 就把所有模型都传进来
        findAllModel(paramVo);
        return modelHistoryDao.getModelUserNumber(paramVo);
    }

    /**
     * 选中的模型名称为空 就把所有模型都传进来
     */
    void findAllModel(EleParamVo paramVo) throws JsonProcessingException {
        if (CollectionUtils.isEmpty(paramVo.getModelIds())) {
            List<ModelConfigVo> modelConfigVoList = getModelList(Long.valueOf(paramVo.getModelId()));
            List<Long> modelIdList = modelConfigVoList.stream().map(ModelConfigVo::getId).collect(Collectors.toList());
            paramVo.setModelIds(modelIdList);
        }
    }

    /**
     * 统计每个模型的问答次数
     */
    public List<ModelPersonResultVo> getModelUseNumber(EleParamVo paramVo) throws JsonProcessingException {
        // 选中的模型名称为空 就把所有模型都传进来
        findAllModel(paramVo);
        // 统计这个实验组的算子每个人跟每个模型对话的总次数
        List<PersonUseCountVo> countVoList = modelHistoryDao.getModelUserCount(paramVo);
        // 找出最大的对话次数
        int maxCount = countVoList.stream().mapToInt(PersonUseCountVo::getNumber).max().orElse(0);
        // 获取最大的阶层数组
        List<Integer> stratumArray = CodeUtil.generateMultiplesOfTen(maxCount);
        List<ModelPersonResultVo> modelPersonResultVos = new ArrayList<>();
        // 遍历数组的最小值到最大值
        for (int i = 0; i < stratumArray.size() - 1; i++) {
            ModelPersonResultVo modelPersonResultVo = new ModelPersonResultVo();
            int finalI = i;
            List<PersonUseCountVo> thisList = countVoList.stream()
                    .filter(person -> person.getNumber() > stratumArray.get(finalI) && person.getNumber() <= stratumArray.get(finalI + 1))
                    .collect(Collectors.toList());
            // 当这一段没有值的时候 就不返回结果了
            if (thisList.isEmpty()) {
                continue;
            }
            Map<Long, List<PersonUseCountVo>> groupedByModelId = thisList.stream()
                    .collect(Collectors.groupingBy(PersonUseCountVo::getModelId));
            List<ModelCountVo> modelCountVos = new ArrayList<>();
            for (Long modelId : paramVo.modelIds) {
                BasicModelEntity basicModelEntity = basicModelDao.selectById(modelId);
                if (groupedByModelId.containsKey(modelId)) {
                    ModelCountVo modelCountVo = new ModelCountVo();
                    modelCountVo.setCountNum(groupedByModelId.get(modelId).size());
                    modelCountVo.setModelName(basicModelEntity.getName());
                    modelCountVo.setModelId(modelId);
                    modelCountVos.add(modelCountVo);
                } else {
                    ModelCountVo modelCountVo = new ModelCountVo();
                    modelCountVo.setModelName(basicModelEntity.getName());
                    modelCountVo.setModelId(modelId);
                    modelCountVos.add(modelCountVo);
                }
            }
            modelPersonResultVo.setModelCountVos(modelCountVos);
            modelPersonResultVo.setMaxNum(stratumArray.get(finalI + 1));
            modelPersonResultVo.setMinNum(stratumArray.get(finalI));
            modelPersonResultVos.add(modelPersonResultVo);
        }
        return modelPersonResultVos;
    }

    /**
     * 统计模型对话平均字数
     */
    public List<ModelPersonResultVo> getAvgInput(EleParamVo paramVo) throws JsonProcessingException {
        List<ModelPersonResultVo> graphList = GraphConstant.USER_AGE_GROUPINGS;
        List<ModelPersonResultVo> modelPersonResultVos = new ArrayList<>();
        // 选中的模型名称为空 就把所有模型都传进来
        findAllModel(paramVo);
        for (ModelPersonResultVo resultVo : graphList) {
            List<ModelCountVo> modelUserVos = new ArrayList<>();
            ModelCountVo modelVo = new ModelCountVo();
            ModelCountVo userVo = new ModelCountVo();
            ModelPersonResultVo modelPersonResultVo = new ModelPersonResultVo();
            modelPersonResultVo.setMinNum(resultVo.minNum);
            modelPersonResultVo.setMaxNum(resultVo.maxNum);
            // 统计每个人的平均输入字数
            List<ModelUserVo> avgUserWord = modelHistoryDao.getAvgUserWord(paramVo);
            // 统计每个模型的平均输入字数
            List<ModelUserVo> avgModelWord = modelHistoryDao.getAvgModelWord(paramVo);
            List<ModelUserVo> thisUserList = avgUserWord.stream()
                    .filter(avg -> avg.getNumber() >= resultVo.minNum && (resultVo.maxNum != 200 ? avg.getNumber() <= resultVo.maxNum : true))
                    .collect(Collectors.toList());
            List<ModelUserVo> thisModelList = avgModelWord.stream()
                    .filter(avg -> avg.getNumber() >= resultVo.minNum && (resultVo.maxNum != 200 ? avg.getNumber() <= resultVo.maxNum : true))
                    .collect(Collectors.toList());
            if (thisUserList.isEmpty() && thisModelList.isEmpty()) {
                continue;
            }
            userVo.setModelName("user");
            userVo.setCountNum(thisUserList.isEmpty() ? 0 : thisUserList.size());
            modelVo.setModelName("model");
            modelVo.setCountNum(thisModelList.isEmpty() ? 0 : thisModelList.size());
            modelUserVos.add(userVo);
            modelUserVos.add(modelVo);
            modelPersonResultVo.setModelCountVos(modelUserVos);
            modelPersonResultVos.add(modelPersonResultVo);
        }
        return modelPersonResultVos;
    }

    /**
     * 统计每个模型的每个人平均使用时长
     */
    public List<ModelUserVo> getAvgUseTime(EleParamVo paramVo) throws JsonProcessingException {
        // 选中的模型名称为空 就把所有模型都传进来
        findAllModel(paramVo);
        // 统计每个模型的平均使用次数
        return modelHistoryDao.getAvgUseTime(paramVo);
    }

}
