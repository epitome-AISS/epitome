package com.nbtech.ailab.facade;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.ExperimentPlanDao;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.QuestionnaireDao;
import com.nbtech.ailab.biz.dao.QuestionnaireDataDao;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.GroupsEntity;
import com.nbtech.ailab.biz.entity.QuestionnaireDataEntity;
import com.nbtech.ailab.biz.entity.QuestionnaireEntity;
import com.nbtech.ailab.biz.service.*;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.common.RedisHeadEnum;
import com.nbtech.ailab.constant.FlowStatus;
import com.nbtech.ailab.constant.QuestionTypeConstant;
import com.nbtech.ailab.constant.CommonConstant;
import com.nbtech.ailab.util.*;
import com.nbtech.ailab.vo.*;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nber
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class QuestionnaireFacade {
    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private IQuestionnaireSelectionService questionnaireSelectionService;

    @Autowired
    private IQuestionnaireScaleService questionnaireScaleService;

    @Autowired
    private IQuestionnaireRecordService questionnaireRecordService;

    @Autowired
    private IQuestionnaireDataService questionnaireDataService;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private IGlobalConfigurationService globalConfigurationService;

    @Autowired
    private QuestionnaireDataDao questionnaireDataDao;

    @Autowired
    private ElementUsedUtil elementUsedUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @Autowired
    @Lazy
    private QuestionnaireFacade self;

    /**
     * 对问卷进行操作 新建/修改问卷
     *
     * @param vo vo
     */
    public void operateQuestionnaire(QuestionnaireVo vo) throws JsonProcessingException {

        // 新建问卷判断是否创建了问题
        if (vo.getId() == null && vo.getQuestions() == null) {
            throw new BizException(BizResponseCodeEnum.NEW_QUESTIONNAIRE_NOT_EMPTY);
        }

        // 对不是草稿状态的问卷进行处理
        if (vo.getStatus() != null && !vo.getStatus().equals(CommonConstant.DRAFT)) {
            throw new BizException(BizResponseCodeEnum.CURRENT_QUESTIONNAIRE_NOT_DRAFT);
        }

        // 校验是否存在都不必填的情况
        List<Integer> must = new ArrayList<>();
        for (QuestionDto q : vo.getQuestions()) {
            if (q.getIsMust() == 1) {
                must.add(1);
            }
        }
        if (must.isEmpty()) {
            throw new BizException(BizResponseCodeEnum.NEW_QUESTIONNAIRE_EXIST_ALL_NOT_MUST);
        }

        // 处理新建问题后的集合
        List<QuestionDto> questions = new ArrayList<>();

        // 对不同类型的问题做不一样的处理
        for (QuestionDto question : vo.getQuestions()) {
            switch (question.getQuestionType()) {
                case "SINGLE_OPTION":
                    if (question.getChoices().isEmpty()) {
                        throw new BizException(BizResponseCodeEnum.CHOICE_CONTEXT_NOT_EMPTY);
                    }
                    question.setQuestionType(QuestionTypeConstant.SINGLE_OPTION);
                    questions.add(question);
                    break;
                case "MULTI_OPTION":
                    if (question.getChoices().size() < 2) {
                        throw new BizException(BizResponseCodeEnum.MULTI_CHOICE_NOT_LESS_TWO);
                    }
                    question.setQuestionType(QuestionTypeConstant.MULTI_OPTION);
                    questions.add(question);
                    break;
                case "SORT":
                    question.setQuestionType(QuestionTypeConstant.SORT);
                    questions.add(question);
                    break;
                case "FILE":
                    question.setQuestionType(QuestionTypeConstant.FILE);
                    questions.add(question);
                    break;
                case "FILL":
                    question.setQuestionType(QuestionTypeConstant.FILL);
                    questions.add(question);
                    break;
                case "SHORT_ANSWER":
                    question.setQuestionType(QuestionTypeConstant.SHORT_ANSWER);
                    questions.add(question);
                    break;
                case "MEASUREMENT":
                    if (question.getScales().isEmpty()) {
                        throw new BizException(BizResponseCodeEnum.SCALES_NOT_EMPTY);
                    }
                    question.setQuestionType(QuestionTypeConstant.MEASUREMENT);
                    questions.add(question);
                    break;
                default:
                    break;
            }
        }

        // 对问题进行处理 转为json格式的字符串
        QuestionDto[] questionNum = questions.toArray(new QuestionDto[0]);
        String jsonString = transferJsonToString(questionNum);
        vo.setQuestionnaireData(jsonString);
        vo.setWorkFlow(FlowStatus.DISABLE);

        // 设置归属人
        SysUserDto user = ShiroUtils.getUserEntity();
        vo.setQuestionnaireAttribution(user.getUsername());
        vo.setUserId(user.getId());

        Long roleId = sysUserRoleService.getByUserId(user.getId());
        vo.setRoleId(roleId);
        vo.setIsDeleted(0);

        // 根据状态处理问卷
        if (vo.getStatus() == null) {
            // 校验 experimentPlanId 必填
            // if (vo.getExperimentPlanId() == null) {
            // throw new
            // BizException(BizResponseCodeEnum.NEW_QUESTIONNAIRE_EXPERIMENT_PLAN_ID_NOT_EMPTY);
            // }

            QuestionnaireDto questionnaireDto = questionnaireService.getByName(vo.getQuestionnaireName());
            if (questionnaireDto != null) {
                throw new BizException(BizResponseCodeEnum.NEW_QUESTIONNAIRE_NOT_REPEAT);
            }
            vo.setStatus(CommonConstant.DRAFT);
            vo.setId(null);
            questionnaireService.save(vo);
        }
        if (vo.getStatus().equals(CommonConstant.DRAFT)) {
            // 校验当前问卷算子能否被修改
            elementUsedUtil.validatePublishElement("collection", vo.getId(), user.getUsername());
            QuestionnaireEntity questionnaireEntity = ConvertUtils.sourceToTarget(vo, QuestionnaireEntity.class);
            questionnaireService.updateById(questionnaireEntity);
        }
    }

    // 将数组对象转换为json格式的字符串
    public String transferJsonToString(Object[] objects) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objects);
        return jsonString;
    }

    /**
     * 问卷状态流程操作
     *
     * @param dto dto
     */
    public void questionnaireFlow(FlowDto dto) {
        QuestionnaireDto questionnaire = questionnaireService.get(dto.getId());

        GlobalConfigurationDto globalConfigurationDto = globalConfigurationService.get(1L);
        Integer isEnableReview = globalConfigurationDto.getIsEnableReview();
        switch (dto.getWorkFlow()) {
            case "ENABLE":
                questionnaire.setStatus(CommonConstant.HAVE_OPEN);
                questionnaire.setWorkFlow(FlowStatus.ENABLE);
                questionnaireService.update(questionnaire);
                break;
            case "DISABLE":
                List<ParamDto> params = groupsDao.getGroupsHaveQuestionnaireIds();
                List<Long> elementIds = params.stream().map(ParamDto::getElementId).distinct()
                        .collect(Collectors.toList());
                for (Long elementId : elementIds) {
                    if (dto.getId().equals(elementId)) {
                        throw new BizException(BizResponseCodeEnum.CURRENT_ELEMENT_HAVE_USED_NOT_DISABLE);
                    }
                }
                questionnaire.setStatus(CommonConstant.DRAFT);
                questionnaire.setWorkFlow(FlowStatus.DISABLE);
                questionnaireService.update(questionnaire);
                break;
            case "OPEN":
                // if (isEnableReview == 1) {
                // questionnaire.setStatus(CommonConstant.WAIT_REVIEW);
                // questionnaire.setWorkFlow(FlowStatus.OPEN);
                // }

                // if (isEnableReview == 0) {
                questionnaire.setStatus(CommonConstant.OPEN);
                questionnaire.setWorkFlow(FlowStatus.OPEN);
                // }

                questionnaireService.update(questionnaire);
                break;
            case "PRIVATE":
                questionnaire.setStatus(CommonConstant.HAVE_OPEN);
                questionnaire.setWorkFlow(FlowStatus.PRIVATE);
                questionnaireService.update(questionnaire);
                questionnaireService.deleteIsReview(dto.getId());
                break;
            default:
                break;
        }
    }

    /**
     * 回答问题
     *
     * @param vo vo
     */
    public void saveQuestionnaireData(QuestionnaireDataVo vo) throws Exception {
        if (vo.getAnswers() == null) {
            throw new BizException(BizResponseCodeEnum.QUESTIONNAIRE_ANSWERS_NOT_EMPTY);
        }

        // 获取对应问卷进行问题回答
        QuestionnaireDto questionnaire = questionnaireService.get(vo.getQuestionnaireId());
        String questionnaireData = questionnaire.getQuestionnaireData();
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建一个答案的集合 后续用作对答案处理好后保存到数据库
        List<AnswerDto> answers = new ArrayList<>();

        // 获取json格式中所有的问题 对每一个问题进行处理
        List<QuestionDto> questions = objectMapper.readValue(questionnaireData, new TypeReference<List<QuestionDto>>() {
        });
        for (QuestionDto q : questions) {
            switch (q.getQuestionType()) {
                case "SINGLE_OPTION":
                    AnswerDto choiceAnswer = vo.getAnswers().stream()
                            .filter(x -> x.getAnswerSort().equals(q.getQuestionSort())).findFirst().get();
                    answers.add(choiceAnswer);
                    break;
                case "MULTI_OPTION":
                    AnswerDto multiAnswer = vo.getAnswers().stream()
                            .filter(x -> x.getAnswerSort().equals(q.getQuestionSort())).findFirst().get();
                    answers.add(multiAnswer);
                    break;
                case "FILE":
                    // 确认保存问卷上传结果 要把上传的文件从临时文件集合中删除 避免上传的文件被定时任务清理
                    Set<Object> objects = redisService.sGet(RedisHeadEnum.QUESTIONNAIRE.getDesc());
                    AnswerDto answerDto = vo.getAnswers().stream()
                            .filter(x -> x.getAnswerSort().equals(q.getQuestionSort())).findFirst().get();
                    if (answerDto.getAnswerContext() != null && CollectionUtils.isNotEmpty(objects)) {
                        redisService.setRemove(RedisHeadEnum.QUESTIONNAIRE.getDesc(), answerDto.getAnswerContext());
                    }
                case "SORT":
                case "FILL":
                case "SHORT_ANSWER":
                    AnswerDto fillAnswer = vo.getAnswers().stream()
                            .filter(x -> x.getAnswerSort().equals(q.getQuestionSort())).findFirst().get();
                    if (q.getIsMust() == 0 && fillAnswer.getAnswerContext() == null) {
                        break;
                    }
                    answers.add(fillAnswer);
                    break;
                case "MEASUREMENT":
                    AnswerDto scaleAnswer = vo.getAnswers().stream()
                            .filter(x -> x.getAnswerSort().equals(q.getQuestionSort())).findFirst().get();
                    if (q.getIsMust() == 0 && scaleAnswer.getScale() == null) {
                        break;
                    }
                    answers.add(scaleAnswer);
                    break;
                default:
                    break;
            }

        }

        // 保存答案
        AnswerDto[] answerNum = answers.toArray(new AnswerDto[0]);
        String jsonString = transferJsonToString(answerNum);
        vo.setQaData(jsonString);
        questionnaireDataService.save(vo);
        // 需要解析问卷的答题数据结果
        QuestionnaireDataEntity questionnaireDataEntity = ConvertUtils.sourceToTarget(vo,
                QuestionnaireDataEntity.class);
        QuestionnaireDto questionnaireDto = questionnaireService.get(vo.getQuestionnaireId());
        List<QuestionDataVo> questionDatas = JSON.parseArray(questionnaireDto.getQuestionnaireData(),
                QuestionDataVo.class);
        QuestionnaireFormatVo questionnaireFormatVo = ParsingQuestionnaireUtil.parsingData(questionnaireDataEntity,
                questionDatas);

        // 问卷选择题结果添加
        questionnaireSelectionService.insertBatch(questionnaireFormatVo.getQuestionnaireSelectionEntities());
        // 问卷量表题结果添加
        questionnaireScaleService.insertBatch(questionnaireFormatVo.getQuestionnaireScaleEntities());
        // 问卷简单填空题结果添加
        questionnaireRecordService.insertBatch(questionnaireFormatVo.getQuestionnaireRecordEntities());

        // 根据实验组id查询实验计划id，异步生成词云
        GroupsEntity group = groupsDao.selectById(vo.getGroupsId());
        if (group != null && group.getExperimentId() != null) {
            self.createWordCloud(vo.getQuestionnaireId(), vo.getGroupsId(), group.getExperimentId());
        }
    }

    /**
     * 文字类型词云结果分析（异步执行）
     *
     * @param questionnaireId 问卷id
     * @param groupsId        实验组id
     * @param experimentId    实验计划id
     */
    @Async
    public void createWordCloud(Long questionnaireId, Long groupsId, Long experimentId) {
        try {
            // 获取当前问卷下所有的填空和问答题
            List<QuestionDto> wordQuestions = questionnaireDataDao.getWords(questionnaireId);
            if (!wordQuestions.isEmpty()) {
                for (QuestionDto q : wordQuestions) {
                    PyParamVo pyParamVo = new PyParamVo();
                    List<String> contexts = questionnaireDao.getWordContextAnswers(groupsId, questionnaireId,
                            q.getQuestionSort());
                    String target = StringUtils.join(contexts, " ");
                    pyParamVo.setExperimentId(experimentId);
                    pyParamVo.setGroupId(groupsId);
                    pyParamVo.setQuestionnaireId(questionnaireId);
                    pyParamVo.setTarget(target);
                    pyParamVo.setQuestionSort(q.getQuestionSort().toString());
                    UsePyUtil.createImage(pyParamVo);
                }
            }
        } catch (Exception e) {
            log.error("异步生成词云失败, questionnaireId={}, groupsId={}, experimentId={}", questionnaireId, groupsId,
                    experimentId, e);
        }
    }

    /**
     * 对问卷进行分页展示
     *
     * @param pageDto pageDto
     * @param dto     dto
     * @return 问卷
     */
    public PageResult<QuestionnairePageDto> pageQuestionnaire(PageDto pageDto, QuestionnairePageDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        PageResult<QuestionnairePageDto> pageResult = questionnaireService.pageQuestionnaire(pageDto, dto, userName,
                roleId);
        return pageResult;
    }

    /**
     * 问卷列表展示
     *
     * @param dto dto
     * @return 列表集合
     */
    public List<QuestionnaireDto> listVo(QuestionnaireDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        List<QuestionnaireDto> list = questionnaireService.listVo(dto, userName);
        return list;
    }

    /**
     * 审核
     *
     * @param dto dto
     */
    public void review(ReviewTestDto dto) {
        QuestionnaireDto questionnaireDto = questionnaireService.get(dto.getId());
        if (dto.getIsReview() == 1) {
            questionnaireDto.setStatus(CommonConstant.OPEN);
            questionnaireDto.setWorkFlow(FlowStatus.OPEN);
            questionnaireDto.setIsReview(dto.getIsReview());
        }
        if (dto.getIsReview() == 0) {
            questionnaireDto.setStatus(CommonConstant.HAVE_OPEN);
            questionnaireDto.setWorkFlow(FlowStatus.ENABLE);
        }
        questionnaireService.update(questionnaireDto);
    }

    /**
     * 删除问卷
     *
     * @param id id
     */
    public void deleteQuestionnaire(Long id) {
        QuestionnaireDto questionnaire = questionnaireService.get(id);
        if (!questionnaire.getStatus().equals(CommonConstant.DRAFT)) {
            throw new BizException(BizResponseCodeEnum.NOT_DRAFT_NOT_DELETE);
        }
        questionnaireService.deleteById(id);

    }

    public PageResult<QuestionnairePageDto> pageAudit(PageDto pageDto, QuestionnairePageDto dto) {
        SysUserDto user = ShiroUtils.getUserEntity();
        String userName = user.getUsername();
        Long roleId = sysUserRoleService.getByUserId(user.getId());
        PageResult<QuestionnairePageDto> pageResult = questionnaireService.pageAudit(pageDto, dto, userName, roleId);
        return pageResult;
    }

    /**
     * 获取数据分析问卷分析回显
     *
     * @param groupId groupId
     * @return 问卷集合
     */
    public List<QuestionnaireDto> getEcho(Long groupId) {
        List<QuestionnaireDto> questionnaireDtoList = questionnaireDataDao.getQuestionnaireIdsByGroupId(groupId);
        for (QuestionnaireDto questionnaire : questionnaireDtoList) {
            QuestionnaireDto dto = questionnaireService.get(questionnaire.getId());
            questionnaire.setQuestionnaireName(dto.getQuestionnaireName());
        }
        return questionnaireDtoList;
    }

}