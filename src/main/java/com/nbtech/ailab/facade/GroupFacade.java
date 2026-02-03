package com.nbtech.ailab.facade;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.*;

import com.nbtech.ailab.util.*;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.utils.ConvertUtils;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.common.model.BizResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nber
 */
@Component
@Slf4j
public class GroupFacade {

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ExperimentDataFacade experimentDataFacade;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Autowired
    private MinioUtil minioUtil;
    private static int expire = 5 * 60;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    private IExperimentPlanService experimentPlanService;

    @Value("${excel.path}")
    private String excelPath;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private IModelService modelService;

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private MaterialFacade materialFacade;

    /**
     * 保存新增的实验组和其元素
     *
     * @param groupsDto 实验组
     * @return
     */
    public GroupsEntity addGroup(GroupsDto groupsDto) throws JsonProcessingException {
        // 新增实验组
        GroupsEntity groupsEntity = BeanUtil.copyProperties(groupsDto, GroupsEntity.class);
        // 给实验组配置秘钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        AesKeyVo aesKeyVo = new AesKeyVo();
        aesKeyVo.setKeyArr(key);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(aesKeyVo);
        // 构建
        groupsEntity.setSecret(jsonString);
        groupsService.insert(groupsEntity);
        groupsEntity.setSecret(null);
        return groupsEntity;
    }

    /**
     * 根据实验计划id获取实验组id集合
     *
     * @param planId
     * @return
     */
    public List<Long> getGroupIds(Long planId) {
        return groupsDao.getGroupIdList(planId);
    }

    /**
     * 复制实验组（包含自己用到的元素） 并把名字变为新的实验组名 -1
     *
     * @param id             实验组 id
     * @param planId         实验计划id
     * @param copyElementMap 存放已经复制过的元素 避免重复使用的元素被重复复制
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public GroupsEntity copyGroup(Long id, Long planId, Map<String, String> copyElementMap)
            throws JsonProcessingException, InterruptedException {
        // 新增实验组
        GroupsEntity groupsEntity = groupsDao.selectById(id);
        if (planId != null) {
            groupsEntity.setExperimentId(planId);
        }
        groupsEntity.setGroupsName(groupsEntity.getGroupsName() + "-1");
        groupsEntity.setId(null);
        // 给实验组配置秘钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        AesKeyVo aesKeyVo = new AesKeyVo();
        aesKeyVo.setKeyArr(key);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(aesKeyVo);
        // 构建
        groupsEntity.setSecret(jsonString);
        if (groupsEntity.getProcessConfig() != null) {
            // 获取实验组的算子信息
            List<ElementVo> elementVoList = groupsService.getElementVo(id);
            // 复制每个算子中的 元素信息
            for (ElementVo elementVo : elementVoList) {
                copyElement(groupsEntity.getExperimentId(), elementVo, copyElementMap);
            }
            // key原验组id value现在实验组id
            Map<String, String> groupIdVoMap = new HashMap<>();
            // key原算子id value现在算子id
            Map<String, String> elementIdVoMap = new HashMap<>();
            // 复制时修改算子的id
            copyGroupConfig(objectMapper, elementVoList, groupIdVoMap, elementIdVoMap);
            // 复制时修改流程的算子id
            groupsEntity.setProcessDag(
                    copyProcessDag(objectMapper, groupsEntity.getProcessDag(), groupIdVoMap, elementIdVoMap, copyElementMap));
            // 复制时修改算子的前端配置
            groupsEntity.setProcessConfig(JSON.toJSONString(elementVoList));
        }
        groupsService.insert(groupsEntity);
        // 不对外展示密钥
        groupsEntity.setSecret(null);
        return groupsEntity;
    }

    /**
     * 复制时修改流程的算子id
     */
    String copyProcessDag(ObjectMapper objectMapper, String processDag, Map<String, String> groupIdVoMap,
                          Map<String, String> elementIdVoMap, Map<String, String> copyElementMap) throws JsonProcessingException {
        ProcessDagVo processDagVo = objectMapper.readValue(processDag, ProcessDagVo.class);
        List<NodeVo> nodes = processDagVo.nodes;
        for (NodeVo nodeVo : nodes) {
            DataVo data = nodeVo.data;
            nodeVo.setId(elementIdVoMap.get(nodeVo.getId()));
            if (ElementTypeEnum.MODEL.getDesc().equals(data.getType())) {
                ConfigVo configVo = objectMapper.convertValue(data.config, ConfigVo.class);
                if (configVo.groupId != null) {
                    String groupId = groupIdVoMap.get(configVo.getGroupId().toString());
                    configVo.setGroupId(Long.valueOf(groupId));
                }

                String modelIdName = generateElementName(ElementTypeEnum.MODEL, Long.valueOf(configVo.getDialogueId()));
                if (copyElementMap.containsKey(modelIdName)) {
                    String copiedModelId = copyElementMap.get(modelIdName);
                    if (copiedModelId != null) {
                        configVo.setDialogueId(Integer.valueOf(copiedModelId));
                    }
                }
                data.config = configVo;
            } else if (ElementTypeEnum.INTERVENE.getDesc().equals(data.getType())) {
                InterveneVo interveneVo = objectMapper.convertValue(data.config, InterveneVo.class);
                if (interveneVo.groupId != null) {
                    String groupId = groupIdVoMap.get(interveneVo.getGroupId().toString());
                    interveneVo.setGroupId(Long.valueOf(groupId));
                }

                String materialIdName = generateElementName(ElementTypeEnum.INTERVENE, interveneVo.getMaterialId());
                if (copyElementMap.containsKey(materialIdName)) {
                    String copiedMaterialId = copyElementMap.get(materialIdName);
                    if (copiedMaterialId != null) {
                        interveneVo.setMaterialId(Long.valueOf(copiedMaterialId));
                    }
                }
                data.config = interveneVo;
            } else if (ElementTypeEnum.COLLECTION.getDesc().equals(data.getType())) {
                CollectionVo collectionVo = objectMapper.convertValue(data.config, CollectionVo.class);

                String collectionIdName = generateElementName(ElementTypeEnum.COLLECTION, collectionVo.getQuestionnaireId());
                if (copyElementMap.containsKey(collectionIdName)) {
                    String copiedCollectionId = copyElementMap.get(collectionIdName);
                    if (copiedCollectionId != null) {
                        collectionVo.setQuestionnaireId(Long.valueOf(copiedCollectionId));
                    }
                }
                data.config = collectionVo;
            }
        }
        List<EdgeVo> edges = processDagVo.edges;
        for (EdgeVo edgeVo : edges) {
            String source = edgeVo.source;
            String target = edgeVo.target;
            edgeVo.source = elementIdVoMap.get(source);
            edgeVo.target = elementIdVoMap.get(target);
            edgeVo.id = edgeVo.id.replace(source, elementIdVoMap.get(source));
            edgeVo.id = edgeVo.id.replace(target, elementIdVoMap.get(target));
        }
        return JSON.toJSONString(processDagVo);
    }

    /**
     * 生成元素名称，格式为：元素类型-目标id
     * 元素的名称等于 类型枚举 + '-' + 元素id
     *
     * @param elementTypeEnum 元素类型枚举
     * @param targetId        目标id
     * @return 格式化后的元素名称，如 "MODEL-123"
     */
    private String generateElementName(ElementTypeEnum elementTypeEnum, Long targetId) {
        return String.format("%S-%d", elementTypeEnum.name(), targetId);
    }

    /**
     * 复制算子中的 元素
     */
    private void copyElement(Long planId, ElementVo elementVo, Map<String, String> copyElementMap)
            throws InterruptedException {
        ElementTypeEnum elementTypeEnum = ElementTypeEnum.fromString(elementVo.getType());
        // 元素的名称等于 类型枚举 + '-' + 元素id
        switch (elementTypeEnum) {
            case MODEL:
                ModelJsonVo modelJsonVo = JSON.parseObject(elementVo.getConfig().toString(), ModelJsonVo.class);
                // 检测这次复制是否已经完成过元素的复制
                String modelIdName = generateElementName(elementTypeEnum, Long.valueOf(modelJsonVo.getDialogueId()));
                if (copyElementMap.containsKey(modelIdName)) {
                    String copiedModelId = copyElementMap.get(modelIdName);
                    if (copiedModelId != null) {
                        modelJsonVo.setDialogueId(copiedModelId);
                    }
                } else {
                    ModelEntity modelEntity = modelService.copyModel(Long.valueOf(modelJsonVo.getDialogueId()), planId);
                    modelJsonVo.setDialogueId(String.valueOf(modelEntity.getId()));
                    copyElementMap.put(modelIdName, String.valueOf(modelEntity.getId()));
                }
                elementVo.setConfig(modelJsonVo);
                break;
            // 算子类型为数据收集
            case COLLECTION:
                // 先复制对应的问卷
                CollectionVo collectionVo = JSON.parseObject(elementVo.getConfig().toString(), CollectionVo.class);
                String collectionIdName = generateElementName(elementTypeEnum, collectionVo.getQuestionnaireId());
                if (copyElementMap.containsKey(collectionIdName)) {
                    String copiedQuestionnaireId = copyElementMap.get(collectionIdName);
                    if (copiedQuestionnaireId != null) {
                        collectionVo.setQuestionnaireId(Long.valueOf(copiedQuestionnaireId));
                    }
                } else {
                    QuestionnaireEntity questionnaireEntity = questionnaireService
                            .copyQuestionnaire(collectionVo.getQuestionnaireId(), planId);
                    collectionVo.setQuestionnaireId(questionnaireEntity.getId());
                    copyElementMap.put(collectionIdName, String.valueOf(questionnaireEntity.getId()));
                }
                elementVo.setConfig(collectionVo);
                break;
            // 算子类型为干预
            case INTERVENE:
                InterventionVo interventionVo = JSON.parseObject(elementVo.getConfig().toString(),
                        InterventionVo.class);
                InterveneVo material = interventionVo.getMaterial();
                String materialIdName = generateElementName(elementTypeEnum, material.getMaterialId());
                if (copyElementMap.containsKey(materialIdName)) {
                    String copiedMaterialId = copyElementMap.get(materialIdName);
                    if (copiedMaterialId != null) {
                        material.setMaterialId(Long.valueOf(copiedMaterialId));
                    }
                } else {
                    MaterialEntity materialEntity = materialFacade.copyMaterial(material.getMaterialId(), planId);
                    material.setMaterialId(materialEntity.getId());
                    copyElementMap.put(materialIdName, String.valueOf(materialEntity.getId()));
                }
                elementVo.setConfig(interventionVo);
                break;
        }
    }

    /**
     * 复制时修改算子的id
     */
    void copyGroupConfig(ObjectMapper objectMapper, List<ElementVo> elementVoList, Map<String, String> groupIdVoMap,
                         Map<String, String> elementIdVoMap) throws InterruptedException {
        for (ElementVo elementVo : elementVoList) {
            Thread.sleep(50);
            String elementId = elementVo.getId();
            String[] elementIdArr = elementId.split("-");
            // 说明这是特殊的群聊算子的id 需要特殊处理
            if (elementIdArr.length > 1) {
                if (groupIdVoMap.containsKey(elementIdArr[0])) {
                    elementVo.setId(groupIdVoMap.get(elementIdArr[0]) + "-" + elementIdArr[1]);
                    // 原算子id 和 新算子id集合
                    elementIdVoMap.put(elementId, groupIdVoMap.get(elementIdArr[0]) + "-" + elementIdArr[1]);
                    setConfig(objectMapper, elementVo, groupIdVoMap, elementIdArr);
                } else {
                    long timeStamp = System.currentTimeMillis();
                    // 存放群聊组对应的新时间戳
                    groupIdVoMap.put(elementIdArr[0], String.valueOf(timeStamp));
                    elementVo.setId(timeStamp + "-" + elementIdArr[1]);
                    elementIdVoMap.put(elementId, timeStamp + "-" + elementIdArr[1]);
                    setConfig(objectMapper, elementVo, groupIdVoMap, elementIdArr);
                }
                // 非特殊算子 直接生成一个时间戳放进去
            } else {
                long timeStamp = System.currentTimeMillis();
                elementVo.setId(String.valueOf(timeStamp));
                elementIdVoMap.put(elementId, String.valueOf(timeStamp));
            }
        }
    }

    // 修改算子的配置字段
    void setConfig(ObjectMapper objectMapper, ElementVo elementVo, Map<String, String> groupIdVoMap,
                   String[] elementIdArr) {
        if (ElementTypeEnum.MODEL.getDesc().equals(elementVo.getType())) {
            ModelJsonVo modelJsonVo = objectMapper.convertValue(elementVo.getConfig(), ModelJsonVo.class);
            modelJsonVo.setGroupId(Long.valueOf(groupIdVoMap.get(elementIdArr[0])));
            elementVo.setConfig(modelJsonVo);
        } else if (ElementTypeEnum.INTERVENE.getDesc().equals(elementVo.getType())) {
            InterventionVo interventionVo = objectMapper.convertValue(elementVo.getConfig(), InterventionVo.class);
            InterveneVo interveneVo = interventionVo.getMaterial();
            interveneVo.setGroupId(Long.valueOf(groupIdVoMap.get(elementIdArr[0])));
            elementVo.setConfig(interventionVo);
        }
    }

    /**
     * 编辑新增的实验组和其元素
     *
     * @param groupsDto 实验组
     * @return
     */
    public GroupsEntity updateGroup(GroupsDto groupsDto) {
        // 修改实验组
        GroupsEntity groupsEntity = BeanUtil.copyProperties(groupsDto, GroupsEntity.class);
        groupsService.updateById(groupsEntity);
        return groupsEntity;
    }

    /**
     * 查询实验计划下的实验组
     *
     * @param planId 实验计划Id
     */
    public PageResult<GroupsEntity> getGroupsByPlan(Page<String> page, String planId) throws JsonProcessingException {
        Page<GroupsEntity> pageResult = groupsDao.getGroupsByPlan(page, planId);
        List<GroupsEntity> targetList = ConvertUtils.sourceToTarget(pageResult.getRecords(), GroupsEntity.class);
        for (GroupsEntity group : targetList) {
            // 转换模型id集合为模型集合
            if (group.getModel() != null && !group.getModel().isEmpty()) {
                getModelList(group);
            }
            // 转换问卷id集合为问卷集合
            if (group.getDataCollection() != null && !group.getDataCollection().isEmpty()) {
                group.setDataCollection(getQuestionList(group.getDataCollection()));
            }
            // 统计实验组实验完成人数
            Long haveNum = groupsPersonDao.selectCount(Wrappers.<GroupsPersonEntity>lambdaQuery()
                    .eq(GroupsPersonEntity::getGroupsId, group.getId())
                    .eq(GroupsPersonEntity::getExperimentStatus, CompletedStatusEnum.END.getDesc()));
            group.setCompleteNumber(haveNum + "/" + group.getGroupsPersonNumber());
        }
        return PageResult.build(page, targetList);
    }

    // 查询模型对话集合下面的所有模型名称集合
    void getModelList(GroupsEntity groupsEntity) throws JsonProcessingException {
        String models = groupsEntity.getModel();
        List<ModelEntity> modelEntities = modelDao.selectList(Wrappers.<ModelEntity>lambdaQuery()
                .in(ModelEntity::getId, models.split(",")));
        ObjectMapper objectMapper = new ObjectMapper();
        Set<String> modelNames = new HashSet<>();
        Set<String> modelIds = new HashSet<>();
        for (ModelEntity entity : modelEntities) {
            if (entity.getModels() != null && !entity.getModels().isEmpty()) {
                // 转json
                List<ModelConfigVo> configList = objectMapper.readValue(entity.getModels(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ModelConfigVo.class));
                configList.forEach(x -> modelNames.add(x.getName()));
                configList.forEach(x -> modelIds.add(String.valueOf(x.getId())));
            }
        }
        groupsEntity.setModel(String.join(",", modelNames));
        groupsEntity.setModelIds(String.join(",", modelIds));

    }

    // 查询问卷对话集合下面的所有问卷名称集合
    String getQuestionList(String questions) {
        List<QuestionnaireEntity> questionnaireEntities = questionnaireDao
                .selectList(Wrappers.<QuestionnaireEntity>lambdaQuery()
                        .in(QuestionnaireEntity::getId, questions.split(",")));
        Set<String> questionnaireNames = new HashSet<>();
        questionnaireEntities.forEach(x -> questionnaireNames.add(x.getQuestionnaireName()));
        return String.join(",", questionnaireNames);
    }

    /**
     * 实验人群包建立
     *
     * @param groupsDto 实验组
     */
    @Transactional(rollbackFor = Exception.class)
    @Async
    public BizResponse<?> insertGroupPerson(GroupsDto groupsDto) throws Exception {
        int needNum = Optional.ofNullable(groupsDto.getGroupsPersonNumber()).isPresent()
                ? groupsDto.getGroupsPersonNumber()
                : 0;
        // 获取实验组已添加过的人数
        if (needNum == 0) {
            // 人数小于0 不能创建
            return BizResponse.exception(new BizException(BizResponseCodeEnum.PERSON_NUMBER_COVER));
        }
        // 用户名前缀
        String prefixName = "EP" + groupsDto.getExperimentId() + "-" + groupsDto.getId();
        commonUtil.createGroupsPerson(groupsDto, needNum, prefixName, 1);

        // 每个聊天室算子生成一个聊天室 并且生成每个聊天室对应人员信息
        groupsService.getElementVo(groupsDto.getId());
        return BizResponse.success();
    }


    /**
     * 实验组额外添加指定数量的人数
     *
     * @param groupId 实验组id
     * @param number  新增的人数
     */
    @Transactional(rollbackFor = Exception.class)
    public BizResponse<?> externalAddGroupPerson(Long groupId, Integer number) throws Exception {
        // 新增实验组的人员数量
        groupsDao.addGroupPerson(number, groupId);

        GroupsDto group = groupsService.get(groupId);
        ExperimentPlanDto plan = experimentPlanService.getPlanById(group.getExperimentId());
        group.setExperimentName(plan.getExperimentName());
        group.setExperimentCode(plan.getExperimentCode());

        // 用户名前缀
        String prefixName = "EP" + group.getExperimentId() + "-" + groupId;

        // 生成实验组人群包
        commonUtil.createGroupsPerson(group, number, prefixName, 0);

        return BizResponse.success();
    }

    /**
     * 图片上传返回url
     */
    public List<String> getFilesUrl(List<MultipartFile> files) {
        List<String> errors = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String url = getFileUrl(file);
                urlList.add(url);
            } catch (Exception e) {
                errors.add(file.getOriginalFilename());
            }
        }
        if (!errors.isEmpty()) {
            // 删除minio中上传成功的文件
            for (String url : urlList) {
                minioUtil.removeUrl(url);
            }
            throw new BizException(BizResponseCodeEnum.IMAGE_UPLOAD_ERROR, String.join(",", errors));
        }
        return urlList;
    }

    public String getFileUrl(MultipartFile file) throws Exception {
        // 随机生成的十位码加上时间戳
        String url = "";
        try {
            // 文件上传
            FileInfoDto fileUrl = minioUtil.getFileUrl(file, null);
            url = fileUrl.getUrl();
        } catch (IOException e) {
            log.error(e.toString());
        }
        // 根据minio客户端账户获取文件url
        return url;
    }

    /**
     * 图片上传返回url
     *
     * @param file 图片
     */
    public String getFilesUrl(MultipartFile file) throws Exception {
        String filename = CodeUtil.getTenCode() + System.currentTimeMillis();
        String[] type = file.getContentType().split("/");
        String lastFormat = "." + type[1];

        try (InputStream inputStream = file.getInputStream();) {
            // 文件上传
            minioUtil.putObject(bucketName, filename + lastFormat, inputStream, file.getContentType());
        } catch (IOException e) {
            log.error(e.toString());
        }
        // 根据minio客户端账户获取文件url
        MinioClient minioClient = minioUtil.getClient();
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .method(Method.GET)
                .expiry(expire)
                .build();
        String minioUrl = minioClient.getPresignedObjectUrl(args);
        return getPrefix(minioUrl, '?') + lastFormat;
    }

    public String getPrefix(String str, char targetChar) {
        int index = str.indexOf(targetChar);
        if (index == -1) {
            return str;
        }
        return str.substring(0, index);
    }

    /**
     * 导出所有实验结果包数据
     *
     * @param dto      参数
     * @param response 结果
     */

    public void exportData(GroupsDto dto, HttpServletResponse response) {
        ExperimentDataVo experimentDataVo = experimentDataFacade.getExperimentData(dto.getExperimentId(), dto.getId());
        ExperimentGroupVo groupVo = groupsDao.getExperimentGroupName(dto.getId());
        try {
            List<UserInfoDto> userInfos = experimentDataVo.getUserInfos();
            // 生成用户信息
            String fileName = groupVo.getExperimentName() + "_" + groupVo.getGroupsName();
            // 默认总文件生成路径
            String mutPath = excelPath;
            // 存放user.xlsx指定的文件夹
            String targetPath = mutPath + FileUtil.FILE_SEPARATOR + fileName;
            // 如果文件夹不存在，创建文件夹
            FileUtil.mkdir(targetPath);
            File file = FileUtils.exportReturnToFile(userInfos, UserInfoDto.class, "temporary.xlsx");
            File newFile = new File(targetPath + FileUtil.FILE_SEPARATOR + "user.xlsx");
            moveFileAsNewFile(file, newFile);
            // 创建DateTimeFormatter对象，指定字符串的格式

            // 生成问卷结果
            List<List<QuestionnaireInfoDto>> questionnaireInfoDtosList = experimentDataVo.getQuestionnaireInfos();
            List<Map<Long, List<Long>>> headInfos = experimentDataVo.getHeadInfos();
            if (headInfos != null && !headInfos.isEmpty()) {
                // 一个map代表一个问卷 mapKey是问卷id value是题目的序号 从零开始 要加一
                int i = 0;
                for (Map<Long, List<Long>> questionnaire : headInfos) {
                    List<List<String>> headList = new ArrayList<List<String>>() {
                        {
                            add(new ArrayList<String>() {
                                {
                                    add("userName");
                                }
                            });
                            add(new ArrayList<String>() {
                                {
                                    add("totalSeconds");
                                }
                            });
                        }
                    };
                    List<List<Object>> dataList = new ArrayList<>();
                    Set<Map.Entry<Long, List<Long>>> entries = questionnaire.entrySet();
                    final Long[] questionnaireId = new Long[1];
                    entries.forEach(x -> {
                        // 问卷的id
                        questionnaireId[0] = x.getKey();
                        x.getValue().forEach(k -> {
                            List<String> questionNum = new ArrayList<>();
                            questionNum.add("Q" + (k + 1));
                            headList.add(questionNum);
                        });
                    });
                    // 这一个问卷中的结果数据
                    List<QuestionnaireInfoDto> questionnaireInfoDtoList = questionnaireInfoDtosList.get(i);
                    for (UserInfoDto userInfo : userInfos) {
                        List<Object> data = new ArrayList<>();
                        List<Long> headIds = questionnaire.get(questionnaireId[0]);
                        // 第一个是用户名
                        data.add(userInfo.getUserName());
                        // 统计数据结果并排序
                        List<QuestionnaireInfoDto> questionnaireInfos = questionnaireInfoDtoList.stream()
                                .filter(x -> x.getQuestionnaireId().equals(questionnaireId[0]) &&
                                        x.getUserId().equals(userInfo.getUserId()) &&
                                        headIds.contains(Long.valueOf(x.getQuestionSort())))
                                .sorted().collect(Collectors.toList());
                        if (!questionnaireInfos.isEmpty()) {
                            // 统计总耗时
                            BigDecimal useTime = groupsService.getElementUseTime(new NextElementVo().builder()
                                    .groupsId(dto.getId())
                                    .experimentId(dto.getExperimentId())
                                    .elementId(questionnaireInfoDtoList.get(0).getElementId())
                                    .userId(userInfo.getUserId())
                                    .build());
                            // 第二个是总耗时
                            data.add(useTime);
                            List<Object> questionData = new ArrayList<>();
                            for (Long headId : headIds) {
                                QuestionnaireInfoDto questionnaireInfoDto = questionnaireInfos.stream()
                                        .filter(x -> x.getQuestionSort() == (headId.intValue())).findFirst()
                                        .orElse(null);
                                if (questionnaireInfoDto == null) {
                                    questionData.add("");
                                } else {
                                    questionData.add(questionnaireInfoDto.getQaData());
                                }
                            }
                            // 保存总的结果数据
                            data.addAll(questionData);
                        }
                        dataList.add(data);
                    }
                    ++i;
                    // 获取问卷的名字 作为excel的名称
                    QuestionnaireEntity questionnaireEntity = questionnaireDao.selectById(questionnaireId[0]);
                    File questionnaireFile = FileUtils.exportReturnToFile(headList, dataList, "temporary.xlsx");
                    File newquestionnaireFile = new File(targetPath + FileUtil.FILE_SEPARATOR
                            + questionnaireEntity.getQuestionnaireName() + "_Questionnaire_" + i + ".xlsx");
                    log.info("本次导出的excel路径是 {}", targetPath + FileUtil.FILE_SEPARATOR
                            + questionnaireEntity.getQuestionnaireName() + "_Questionnaire_" + i + ".xlsx");
                    log.info("准备用来保存临时文件的文件是否存在 {}", newquestionnaireFile.exists());
                    moveFileAsNewFile(questionnaireFile, newquestionnaireFile);
                }
            }

            // 生成模型问答结果
            List<List<ModelInfoDto>> modelInfoDtosList = experimentDataVo.getModelInfos();
            if (modelInfoDtosList != null && !modelInfoDtosList.isEmpty()) {
                int index = 0;
                for (List<ModelInfoDto> modelInfoDtos : modelInfoDtosList) {
                    ++index;
                    if (modelInfoDtos.isEmpty()) {
                        continue;
                    }
                    String modelInfoPath = modelInfoDtos.get(0).getElementName() + "_" + index
                            + "_Conversation_History";
                    // 如果文件夹不存在，创建文件夹
                    // FileUtil.mkdir(modelInfoPath);
                    Map<Long, List<ModelInfoDto>> modelInfoMap = modelInfoDtos.stream()
                            .collect(Collectors.groupingBy(ModelInfoDto::getUserId));
                    List<ModelInfoDto> modelInfoDtoList = new ArrayList<>();
                    for (Long userId : modelInfoMap.keySet()) {
                        String userName = Objects.requireNonNull(
                                        userInfos.stream().filter(x -> x.getUserId().equals(userId)).findFirst().orElse(null))
                                .getUserName();
                        List<ModelInfoDto> shortList = modelInfoMap.get(userId);
                        Long i = 1L;
                        // 给每个excel记录编个序号
                        for (ModelInfoDto modelInfo : shortList) {
                            modelInfo.setModelId(i);
                            modelInfo.setUserName(userName);
                            ++i;
                        }
                        modelInfoDtoList.addAll(shortList);
                    }
                    File modelInfoFile = FileUtils.exportReturnToFile(modelInfoDtoList, ModelInfoDto.class,
                            "temple.xlsx");
                    File newModelInfoFile = new File(targetPath + FileUtil.FILE_SEPARATOR + modelInfoPath + ".xlsx");
                    moveFileAsNewFile(modelInfoFile, newModelInfoFile);
                }
            }

            // 聊天室的聊天记录excel
            List<RoomChatHistoryExcelVo> chatHistoryExcelVos = experimentDataVo.getChatHistoryExcelVos();
            if (CollectionUtils.isNotEmpty(chatHistoryExcelVos)) {
                File modelInfoFile = FileUtils.exportReturnToFile(chatHistoryExcelVos, RoomChatHistoryExcelVo.class,
                        "temple.xlsx");
                File newModelInfoFile = new File(
                        targetPath + FileUtil.FILE_SEPARATOR + groupVo.getExperimentName() + "chatRoom.xlsx");
                moveFileAsNewFile(modelInfoFile, newModelInfoFile);
            }

            // 实验组执行的素材算子记录
            List<MaterialGroupListDto> materialGroupListDtos = experimentDataVo.getMaterialGroupListDtoList();
            if (CollectionUtils.isNotEmpty(materialGroupListDtos)) {
                File modelInfoFile = FileUtils.exportReturnToFile(materialGroupListDtos, MaterialGroupListDto.class,
                        "temple.xlsx");
                File newModelInfoFile = new File(targetPath + FileUtil.FILE_SEPARATOR + groupVo.getExperimentName()
                        + "materialGroupUseRecord.xlsx");
                moveFileAsNewFile(modelInfoFile, newModelInfoFile);
            }

            // 问卷星的数据收集excel
            List<List<QuestionStarDataEntity>> questionStarDataEntityList = experimentDataVo
                    .getQuestionStarDataEntityList();
            if (CollectionUtils.isNotEmpty(questionStarDataEntityList)) {
                for (List<QuestionStarDataEntity> questionStarDataEntities : questionStarDataEntityList) {
                    List<QuestionStarDataExcelVo> questionStarDataExcelVos = ConvertUtils
                            .sourceToTarget(questionStarDataEntities, QuestionStarDataExcelVo.class);
                    // 转换识别一下问卷星数据
                    QuestionStarDataAnswerVo questionData = changeQStart(questionStarDataExcelVos);
                    File questionStarFile = FileUtils.exportReturnToFile(questionData.getAnswerHeadList(),
                            questionData.getAnswerDataList(), "temple.xlsx");
                    QuestionStarDataExcelVo questionStarDataExcelVo = questionStarDataExcelVos.get(0);

                    String excelName = questionStarDataExcelVo.getActivity() + "-" + questionStarDataExcelVo.getName()
                            + "-";
                    File newModelInfoFile = new File(
                            targetPath + FileUtil.FILE_SEPARATOR + excelName + "questionStar.xlsx");
                    moveFileAsNewFile(questionStarFile, newModelInfoFile);
                }
            }

            // 获取合作测评数据
            List<PushDataVo> pushDataVoList = experimentDataVo.getPushDataVoList();
            int i = 1;
            for (PushDataVo pushDataVo : pushDataVoList) {
                List<InitialPushEntity> initialPushEntityList = pushDataVo.getInitialPushEntityList();
                if (CollectionUtils.isNotEmpty(initialPushEntityList)) {
                    String excelName = pushDataVo.getElementId() + "-" + "initialPush" + i;
                    File questionStarFile = FileUtils.exportReturnToFile(initialPushEntityList, InitialPushEntity.class,
                            "temple.xlsx");
                    File newModelInfoFile = new File(targetPath + FileUtil.FILE_SEPARATOR + excelName + ".xlsx");
                    moveFileAsNewFile(questionStarFile, newModelInfoFile);
                }
                List<SelectPushEntity> selectPushEntityList = pushDataVo.getSelectPushEntityList();
                if (CollectionUtils.isNotEmpty(selectPushEntityList)) {
                    String excelName = pushDataVo.getElementId() + "-" + "selectPush" + i;
                    File selectPushFile = FileUtils.exportReturnToFile(selectPushEntityList, SelectPushEntity.class,
                            "temple.xlsx");
                    File newSelectPushFileFile = new File(targetPath + FileUtil.FILE_SEPARATOR + excelName + ".xlsx");
                    moveFileAsNewFile(selectPushFile, newSelectPushFileFile);
                }
                List<ScorePushEntity> scorePushEntityList = pushDataVo.getScorePushEntityList();
                if (CollectionUtils.isNotEmpty(scorePushEntityList)) {
                    String excelName = pushDataVo.getElementId() + "-" + "scorePush" + i;
                    File scorePushFile = FileUtils.exportReturnToFile(scorePushEntityList, ScorePushEntity.class,
                            "temple.xlsx");
                    File newScorePushFileFile = new File(targetPath + FileUtil.FILE_SEPARATOR + excelName + ".xlsx");
                    moveFileAsNewFile(scorePushFile, newScorePushFileFile);
                }
                i = i++;
            }
            // 压缩某个文件夹路径下的所有文件
            File result = ZipUtil.zip(mutPath, CharsetUtil.CHARSET_UTF_8);
            // 把打包的压缩文件放到response里面
            FileUtils.downloadZip(result, response, fileName);
            // 删除临时文件夹下所有文件和临时文件夹
            FileUtils.deletePath(mutPath);
            // 删除压缩文件
            result.delete();
        } catch (Exception e) {
            log.error("文件压缩异常", e);
        }
    }

    void moveFileAsNewFile(File targetFile, File newFile) {
        // 使用 Files.move() 替代 renameTo()
        try {
            Path source = targetFile.toPath();
            Path target = newFile.toPath();
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("文件移动成功: {}", target);
        } catch (Exception e) {
            log.error("文件移动失败", e);
            try {
                Files.copy(targetFile.toPath(), newFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                targetFile.delete();
                log.info("文件复制成功");
            } catch (IOException ex) {
                log.error("文件复制也失败", ex);
            }
        }
    }

    /**
     * 解析问卷星答题数据
     *
     * @param excelVos 答题结果
     */
    QuestionStarDataAnswerVo changeQStart(List<QuestionStarDataExcelVo> excelVos) {
        QuestionStarDataAnswerVo questionStarDataAnswerVo = new QuestionStarDataAnswerVo();
        List<List<String>> questionStartHead = new ArrayList<>();
        List<List<Object>> answerDataListList = new ArrayList<>();
        questionStarDataAnswerVo.setAnswerHeadList(questionStartHead);
        questionStarDataAnswerVo.setAnswerDataList(answerDataListList);

        for (QuestionStarDataExcelVo questionStarDataExcelVo : excelVos) {
            List<QsDataQuestionVo> qsDataQuestionVos = new ArrayList<>();

            // 转换题目信息为题目对象集合
            String questionTitle = questionStarDataExcelVo.getQuestionTitle();
            String[] titleArr = questionTitle.split("<br/>");
            String qsTitle = "";
            for (String title : titleArr) {
                String[] titleDataArr = title.split(":");
                if ("title".equals(titleDataArr[0])) {
                    continue;
                }
                // 是题目
                if (!titleDataArr[0].contains("#") && !titleDataArr[0].contains("_")) {
                    QsDataQuestionVo qsDataQuestionVo = new QsDataQuestionVo();
                    qsDataQuestionVo.setQuestion(titleDataArr[0]);
                    qsDataQuestionVo.setQuestionText(titleDataArr[1]);
                    qsTitle = titleDataArr[0];
                    qsDataQuestionVos.add(qsDataQuestionVo);
                } else {
                    QsDataChooseVo qsDataChooseVo = new QsDataChooseVo();
                    String[] chooseArr = title.split(":");
                    // qsDataChooseVo.setQuestion(qsTitle);
                    qsDataChooseVo.setChooseData(chooseArr[1]);
                    String finalQsTitle = qsTitle;
                    QsDataQuestionVo qsDataQuestionVo = qsDataQuestionVos.stream()
                            .filter(x -> finalQsTitle.equals(x.getQuestion())).findFirst().get();
                    List<QsDataChooseVo> questionChoices = qsDataQuestionVo.getQuestionChoices();
                    if (CollectionUtils.isNotEmpty(questionChoices)) {
                        questionChoices.add(qsDataChooseVo);
                        qsDataQuestionVo.setQuestionChoices(questionChoices);
                    } else {
                        List<QsDataChooseVo> newQuestionChoices = new ArrayList<>();
                        newQuestionChoices.add(qsDataChooseVo);
                        qsDataQuestionVo.setQuestionChoices(newQuestionChoices);
                    }
                }
            }

            // 转换答案集合为答案数据
            String questionData = questionStarDataExcelVo.getQuestionData();
            JSONObject questionDataJson = JSON.parseObject(questionData);
            Map<String, String> needDataMap = new HashMap<>();
            for (QsDataQuestionVo questionVo : qsDataQuestionVos) {
                String question = questionVo.getQuestion();
                Set<String> questionKey = questionDataJson.keySet();
                Set<String> needKey = new HashSet<>();

                for (String key : questionKey) {
                    // 检查key是否以question开头
                    if (key.startsWith(question)) {
                        // 获取question后面的第一个字符（如果存在）
                        if (key.length() > question.length()) {
                            char nextChar = key.charAt(question.length());
                            // 检查这个字符是否不是数字
                            if (!Character.isDigit(nextChar)) {
                                needKey.add(key);
                            }
                        } else {
                            needKey.add(key);
                        }
                    }
                }

                List<String> keyDataList = new ArrayList<>();
                for (String key : needKey) {
                    String keyData = (String) questionDataJson.get(key);
                    Boolean isNum = false;
                    try {
                        Integer.parseInt(keyData);
                        isNum = true;
                    } catch (NumberFormatException e) {
                        isNum = false;
                    }
                    if (keyData.contains(",") || isNum) {
                        // 存在 , 疑似多选题的选项
                        String[] keyDataArr = keyData.split(",");
                        List<Integer> keyDataIntList = new ArrayList<>();
                        for (String str : keyDataArr) {
                            try {
                                // 尝试将字符串解析为整数 如果这个字符串不是纯数字组成的 1,2,3 直接认定为不是选择题选项
                                int num = Integer.parseInt(str);
                                keyDataIntList.add(num);
                            } catch (NumberFormatException e) {
                                keyDataIntList.clear();
                                break;
                            }
                        }

                        if (questionVo.getQuestionChoices() != null) {
                            List<QsDataChooseVo> questionChoices = questionVo.getQuestionChoices();
                            for (Integer choicesIndex : keyDataIntList) {
                                if ((choicesIndex - 1) > questionChoices.size()) {
                                    keyDataList.add(String.valueOf(choicesIndex));
                                } else {
                                    QsDataChooseVo qsDataChooseVo = questionChoices.get(choicesIndex - 1);
                                    String chooseData = qsDataChooseVo.getChooseData();
                                    keyDataList.add(chooseData);
                                }
                            }
                        } else {
                            keyDataList.add(keyData);
                        }
                    } else {
                        keyDataList.add(keyData);
                    }
                }
                String needData = String.join(",", keyDataList);
                needDataMap.put(question, needData);
            }

            questionStarDataExcelVo.setQuestionTitle(JSON.toJSONString(qsDataQuestionVos));

            List<Integer> keyIntegers = new ArrayList<>();
            for (String key : needDataMap.keySet()) {
                String intNumString = key.replace("q", "");
                keyIntegers.add(Integer.parseInt(intNumString));
            }
            Collections.sort(keyIntegers);
            List<Object> answerData = new ArrayList<>();
            // 表头仅添加一次
            if (questionStartHead.isEmpty()) {
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("activity Id");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("activity Name");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("ipaddress");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("username");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("province");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("city");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("question Title");
                    }
                });
                for (Integer key : keyIntegers) {
                    // 把 sortedMap key做表头
                    questionStartHead.add(new ArrayList<String>() {
                        {
                            add("q" + key);
                        }
                    });
                }
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("use Time");
                    }
                });
                questionStartHead.add(new ArrayList<String>() {
                    {
                        add("submit Time");
                    }
                });
            }
            answerData.add(questionStarDataExcelVo.getActivity());
            answerData.add(questionStarDataExcelVo.getName());
            answerData.add(questionStarDataExcelVo.getIpaddress());
            answerData.add(questionStarDataExcelVo.getUserName());
            answerData.add(questionStarDataExcelVo.getProvince());
            answerData.add(questionStarDataExcelVo.getCity());
            answerData.add(questionStarDataExcelVo.getQuestionTitle());
            for (Integer key : keyIntegers) {
                // 把 sortedMap key做表头
                answerData.add(needDataMap.get("q" + key));
            }
            answerData.add(questionStarDataExcelVo.getTimetaken());
            answerData.add(questionStarDataExcelVo.getSubmittime());
            answerDataListList.add(answerData);
        }

        return questionStarDataAnswerVo;
    }

}
