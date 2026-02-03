package com.nbtech.ailab.facade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.RoomRoleTypeEnum;
import com.nbtech.ailab.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExperimentDataFacade {
    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private QuestionnaireSelectionDao questionnaireSelectionDao;

    @Autowired
    private QuestionnaireScaleDao questionnaireScaleDao;

    @Autowired
    private QuestionnaireRecordDao questionnaireRecordDao;

    @Autowired
    private ModelHistoryDao modelHistoryDao;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private QuestionnaireDataDao questionnaireDataDao;

    @Autowired
    private ChatHistoryDetailDao chatHistoryDetailDao;

    @Autowired
    private BasicModelDao basicModelDao;

    @Autowired
    private ExperimentProgressDao experimentProgressDao;

    @Autowired
    private IQuestionStarDataService questionStarDataService;

    @Autowired
    private IInitialPushService initialPushService;


    /**
     * 获取实验组数据包
     */
    public ExperimentDataVo getExperimentData(Long experimentId, Long groupId) {
        //创建一个实验组数据包vo
        ExperimentDataVo vo = new ExperimentDataVo();
        List<List<QuestionnaireInfoDto>> questionnaireInfos = new ArrayList<>();
        List<List<ModelInfoDto>> modelInfos = new ArrayList<>();
        List<Map<Long, List<Long>>> scaleHead = new ArrayList<>();

        //获取用户信息
        List<UserInfoDto> userInfos = groupsPersonService.getUserInfo(experimentId, groupId);

        //获取问卷算子的信息 查询普通问卷答题结果
        List<QuestionElementVo> questionnaireIds = getQuestionnaireIds(groupId);

        if (!questionnaireIds.isEmpty()) {
            for (QuestionElementVo qeVo : questionnaireIds) {
                List<QuestionnaireInfoDto> totalList = new ArrayList<>();
                List<QuestionnaireInfoDto> newList = new ArrayList<>();
                Map<Long, List<Long>> map1 = new HashMap<>();
                List<Long> scaleSorts = questionnaireDataDao.getSortsByQuestionnaireId(qeVo.getQuestionnaireId());
                map1.put(qeVo.getQuestionnaireId(), scaleSorts);

                List<QuestionnaireInfoDto> questionnaireOptionList = questionnaireSelectionDao.getOptionData(groupId, qeVo.getQuestionnaireId(), qeVo.getElementId());
                List<QuestionnaireInfoDto> questionnaireWordList = questionnaireRecordDao.getRecordData(groupId, qeVo.getQuestionnaireId(), qeVo.getElementId());
                List<QuestionnaireInfoDto> questionnaireScaleList = questionnaireScaleDao.getScaleData(groupId, qeVo.getQuestionnaireId(), qeVo.getElementId());
                if (!questionnaireOptionList.isEmpty()) {
                    totalList.addAll(questionnaireOptionList);
                }
                if (!questionnaireWordList.isEmpty()) {
                    totalList.addAll(questionnaireWordList);
                }
                if (!questionnaireScaleList.isEmpty()) {
                    totalList.addAll(questionnaireScaleList);
                }
                if (!totalList.isEmpty()) {
                    for (QuestionnaireInfoDto q : totalList) {
                        q.setElementId(qeVo.getElementId());
                        newList.add(q);
                    }
                }

                questionnaireInfos.add(newList);
                scaleHead.add(map1);
            }
        }


        //获取当前实验组下所有大模型算子id集合（大模型）
        List<String> idsList = getDialogueElementIds(groupId);
        //获取以大模型id为键 对应基础模型为值的map集合
        Map<String, QuElementDto> elementMap = getDIds(groupId);
        //对当前实验组下所有的大模型进行遍历 获取其中对应的基础模型对话信息
        if (!idsList.isEmpty()) {
            for (String elementId : idsList) {
                QuElementDto quElementDto = elementMap.get(elementId);
                String elementName = modelDao.selectById(quElementDto.getQuestionnaireId()).getModelName();
                List<ModelInfoDto> totalList = modelHistoryDao.getModelInfo(groupId, quElementDto.getId());
                totalList.forEach(x -> x.setElementName(elementName));
                if (!totalList.isEmpty()) {
                    modelInfos.add(totalList);
                }
            }
        }
        // 查询这个实验组下的所有聊天室算子
        List<String> elementIds = groupsService.getRoomElementIds(groupId);
        List<RoomChatHistoryExcelVo> allRecode = new ArrayList<>();
        for (String elementId : elementIds) {
            // 获取当前实验组下的聊天记录
            List<RoomChatHistoryExcelVo> roomChatRecords = chatHistoryDetailDao.getRoomChatRecord(elementId);
            roomChatRecords.forEach(x -> {
                // 模型去基础模型查询用户名
                if (RoomRoleTypeEnum.MODEL.name().equals(x.getRoleType())) {
                    String basicModelName = basicModelDao.getBasicName(x.getUserId());
                    x.setUsername(basicModelName);
                    // 模型不能做主持人
                    x.setIsHost(false);
                }
            });
            allRecode.addAll(roomChatRecords);
        }

        // 查询这个实验组下的所有素材包算子id集合(无序)
        List<String> materialGroupElementId = groupsService.getMaterialGroupElementId(groupId);
        // 根据算子id查询执行的素材信息
        if (!materialGroupElementId.isEmpty()) {
            List<MaterialGroupListDto> materialGroup = experimentProgressDao.getMaterialGroup(groupId, materialGroupElementId);
            vo.setMaterialGroupListDtoList(materialGroup);
        }

        // 获取这个实验组的所有 问卷星答题结果
        GroupsDto groupsDto = groupsService.get(groupId);
        List<List<QuestionStarDataEntity>> elementStarData = questionStarDataService.getElementStarData(groupsDto.getProcessConfig());

        // 获取这个实验组下问卷
        List<ProcessQuestionnaireVo> processQuestionnaireList = questionStarDataService.getProcessQuestionnaire(groupId);
        Map<ProcessQuestionnaireVo,List<ProcessQuestionnaireExcelVo>> processQuestionnaireVoListMap = new HashMap<>();
        for (ProcessQuestionnaireVo questionnaireVo : processQuestionnaireList){
            // 获取这个流程这个问卷的答题结果详情
            List<ProcessQuestionnaireExcelVo> processData = questionStarDataService.getProcessData(groupId, questionnaireVo.getQuestionnaireId(), questionnaireVo.getElementId(), questionnaireVo.getProcessId());
            processQuestionnaireVoListMap.put(questionnaireVo, processData);
        }

        // 获取这个实验组下的所有合作测评推送的数据
        List<PushDataVo> pushData = initialPushService.getPushData(groupsDto.getProcessConfig());

        vo.setQuestionStarDataEntityList(elementStarData);
        vo.setModelInfos(modelInfos);
        vo.setChatHistoryExcelVos(allRecode);
        vo.setUserInfos(userInfos);
        vo.setQuestionnaireInfos(questionnaireInfos);
        vo.setQuestionnaireExcelVoMap(processQuestionnaireVoListMap);
        vo.setHeadInfos(scaleHead);
        vo.setPushDataVoList(pushData);
        return vo;
    }

    /**
     * 获取实验组下问卷id集合
     *
     * @param groupId 实验组id
     * @return 问卷算子id集合
     */
    private List<QuestionElementVo> getQuestionnaireIds(Long groupId) {
        List<QuestionElementVo> questionElementVos = new ArrayList<>();
        //根据实验组id获取实验组信息
        GroupsDto groupsDto = groupsService.get(groupId);
        String processConfig = groupsDto.getProcessConfig();
        JSONArray jsonArray = JSON.parseArray(processConfig);
        //遍历json数组中所有的对象
        for (int i = 0; i < jsonArray.size(); i++) {
            QuestionElementVo questionElementVo = new QuestionElementVo();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            questionElementVo.setElementId(String.valueOf(jsonObject.get("id")));
            questionElementVo.setSequence(jsonObject.getInteger("sequence"));
            Long questionnaireId = jsonObject.getJSONObject("config").getLong("questionnaireId");
            if (questionnaireId != null) {
                questionElementVo.setQuestionnaireId(questionnaireId);
                questionElementVos.add(questionElementVo);
            }
        }
        Collections.sort(questionElementVos, (o1, o2) -> Integer.compare(o1.getSequence(), o2.getSequence()));
        return questionElementVos;

    }

    /**
     * 获取实验组下问卷算子id集合
     *
     * @param groupId groupId
     * @return 问卷算子id集合
     */
    private Map<Long, QuElementDto> getIds(Long groupId) {
        GroupsDto groupsDto = groupsService.get(groupId);
        String processConfig = groupsDto.getProcessConfig();
        JSONArray jsonArray = JSON.parseArray(processConfig);
        Map<Long, QuElementDto> map = new HashMap<>();
        //遍历json数组中所有的对象
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            QuElementDto quElementDto = new QuElementDto();
            String elementId = jsonObject.getString("id");
            Long questionnaireId = jsonObject.getJSONObject("config").getLong("questionnaireId");
            quElementDto.setId(elementId);
            quElementDto.setQuestionnaireId(questionnaireId);
            if (type.equals("collection")) {
                map.put(questionnaireId, quElementDto);
            }
        }

        return map;
    }

    /**
     * 获取实验组下模型对话算子id集合
     *
     * @param groupId groupId
     * @return 模型对话算子id集合
     */
    private Map<String, QuElementDto> getDIds(Long groupId) {
        GroupsDto groupsDto = groupsService.get(groupId);
        String processConfig = groupsDto.getProcessConfig();
        JSONArray jsonArray = JSON.parseArray(processConfig);
        Map<String, QuElementDto> map = new HashMap<>();
        //遍历json数组中所有的对象
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            QuElementDto quElementDto = new QuElementDto();
            String elementId = jsonObject.getString("id");
            Long dialogueId = jsonObject.getJSONObject("config").getLong("dialogueId");
            quElementDto.setId(elementId);
            quElementDto.setQuestionnaireId(dialogueId);
            if (type.equals("dialogue")) {
                map.put(elementId, quElementDto);
            }
        }

        return map;
    }


    /**
     * 获取实验组下大模型算子id集合
     *
     * @param groupId groupId
     * @return 大模型算子id集合
     */
    private List<String> getDialogueElementIds(Long groupId) {
        List<String> dialogueIds = new ArrayList<>();
        GroupsDto groupsDto = groupsService.get(groupId);
        String processConfig = groupsDto.getProcessConfig();
        JSONArray jsonArray = JSON.parseArray(processConfig);
        //遍历json数组中所有的对象
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            String elementId = jsonObject.getString("id");
            if (type.equals("dialogue")) {
                dialogueIds.add(elementId);
            }
        }

        return dialogueIds;
    }


}
