package com.nbtech.ailab.common;

import com.nbtech.common.enums.IResponseCode;

/**
 * @author van
 */

public enum BizResponseCodeEnum implements IResponseCode {
    NEW_QUESTIONNAIRE_NOT_EMPTY(1000, "新建问卷题目不能为空"),
    CHOICE_CONTEXT_NOT_EMPTY(1001, "选项类型题目下选项内容不能为空"),
    MULTI_CHOICE_NOT_LESS_TWO(1002, "多选题目类型下选项不能少于两个"),
    SCALES_NOT_EMPTY(1003, "量表内容不能为空"),
    CURRENT_QUESTIONNAIRE_NOT_DRAFT(1004, "当前问卷不为草稿状态，无法修改"),
    NEW_QUESTIONNAIRE_EXIST_ALL_NOT_MUST(1005, "新建问卷中题型不能均为不必填形式"),
    CHOICES_ANSWER_NOT_EMPTY(1006, "当前选项类型问题答案不能为空"),
    QUESTIONNAIRE_ANSWERS_NOT_EMPTY(1007, "问卷答案为空,请填写相关问卷题目答案"),
    CONTEXT_NOT_EMPTY(1008, "当前问答类型问题答案不能为空"),
    SCALE_GRADE_NOT_EMPTY(1009, "当前量表类型问题答案不能为空"),
    SINGLE_CHOICE_MULTI(1010, "单选选项题目答案不能选择多个"),
    MODELS_NUM_NOT_EMPTY(1011, "新建模型对话中基础模型数量不能为空"),
    MODELS_PROBABILITIES_NOT_ONE(1012, "模型选用概率加起来总和需要为1"),
    MODEL_PROBABILITY_BETWEEN_ZERO_AND_ONE(1013, "模型选用概率填写需保持在0到1之间"),
    TEMPERATURE_BETWEEN_ZERO_AND_ONE(1014, "温度填写需保持在0到1之间"),
    MODEL_NAME_NOT_REPEAT(1015, "同一用户下模型名称不能重复"),
    SINGLE_MODEL_NOT_MULTI(1016, "单一模型数量基础模型不能为多个"),
    MULTI_MODEL_NOT_SINGLE(1017, "多个随机模型数量基础模型不能为单个"),
    NEW_QUESTIONNAIRE_NOT_REPEAT(1018, "同一用户下新建问卷名字不能重复"),
    NEW_MATERIAL_NOT_REPEAT(1019, "同一用户下新建素材名字不能重复"),
    NEW_MATERIAL_NAME_NOT_EMPTY(1020, "新建素材素材名称不能为空"),
    NOT_DRAFT_NOT_DELETE(1021, "当前不是草稿状态，无法删除"),
//    NEW_MATERIAL_EXPERIMENT_PLAN_ID_NOT_EMPTY(1026, "新建素材归属实验计划id不能为空"),
    MATERIAL_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY(1027, "查询素材列表时归属实验计划id不能为空"),
//    NEW_QUESTIONNAIRE_EXPERIMENT_PLAN_ID_NOT_EMPTY(1028, "新建问卷归属实验计划id不能为空"),
    QUESTIONNAIRE_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY(1029, "查询问卷列表时归属实验计划id不能为空"),
//    NEW_MODEL_EXPERIMENT_PLAN_ID_NOT_EMPTY(1030, "新建模型归属实验计划id不能为空"),
    MODEL_LIST_EXPERIMENT_PLAN_ID_NOT_EMPTY(1031, "查询模型列表时归属实验计划id不能为空"),
    CURRENT_ELEMENT_HAVE_BEEN_USED(1022, "当前算子已经被实验组发布，无法修改"),
    CURRENT_ELEMENT_HAVE_USED_NOT_DISABLE(1023, "当前算子已经被实验组引用，无法禁用"),
    ELEMENT_NOT_ONLY_USED_BY_OWN(1024, "当前算子已被其他实验组创建者引用，无法修改"),
    SINGLE_MODEL_NOT_SINGLE(1025, "单个随机模型数量基础模型不能为单个"),
    NOT_BESUBMIT(1, "非待提交不可提交！"),
    NOT_BEAUDIT(2, "非待审核不可审核！"),
    NOT_BEPUBLISH(3, "非待发布不可发布！"),
    NOT_BEEND(4, "非待完成不可完成！"),

    USER_NOT_EXIST(9001, "该用户不存在！"),

    PERSON_NUMBER_COVER(9010, "实验人数不足1人"),

    MEET_PERSON_NUMBER(9011, "实验人数已满足需求"),

    EXPERIMENT_NAME_REPEAT(9012, "实验名称重复"),

    /**
     * 对应模型不存在
     */
    EXISTS_NOT_MODEL(8011, "对应模型不存在"),

    /**
     * 对应实验组不存在
     */
    EXISTS_NOT_GROUP(8012, "实验组不存在"),

    /**
     * 对应模型算子不存在
     */
    EXISTS_NOT_ELEMENT(8013, "对应算子不存在 请联系管理员"),
    /**
     * 实验组数量不足
     */
    GROUP_SHORTAGE(8014, "实验组数量不足预期"),

    /**
     * 实验组数量超过
     */
    GROUP_COVER(8015, "实验组数量超过预期"),

    /**
     * 用户Id没传
     */
    PERSON_USERID(8018, "需要用户id"),

    /**
     * 未通过伦理协会审核不可发布
     */
    ETHICS_AUDIT(8019, "未通过伦理协会审核不可发布"),
    /**
     * 用户已登录 不可重复登录
     */
    USER_LOGIN(8020, "The user name is logging in"),

    /**
     * 未知错误 请联系管理员
     */
    GLOBAL_ERROR(8021, "未知错误,请联系管理员"),

    /**
     * 实验未开始
     */
    NOT_YET_DUE(8022, "实验未开始"),

    /**
     * 角色权限不可访问
     */
    PERMISSION_DENIED(8023, "当前登录用户不可访问该资源"),

    USERNAME_PASSWORD_ERROR(8024, "用户名或密码错误，请重新登录!"),

    USER_FROZEN(8025, "用户冻结！"),

    USERNAME_REPEAT(8026, "用户名重复"),

    BASIC_MODEL_FORBID(8030, "已使用的基础模型不可禁用"),

    BASIC_MODEL_VERIFY(8031, "基础模型未通过可用性校验，请核对后创建"),

    DIFY_SERVICE_ERROR(8032, "dify服务异常,请联系管理员"),

    WORKFLOW_RUN_ERROR(8034, "工作流运行异常,请检测运行参数或工作流配置是否正确"),

    IMAGE_UPLOAD_ERROR(8036, "文件 {0} 上传失败！"),

    CHAT_ROOM_CONNECTION_ERROR(8038, "聊天室未连接,请刷新后重试"),

    TEACHING_PLAN_NOT_EXIST_PERSONGROUP(8039, "教案不存在人群包,无法下载人群包数据"),

    WORK_FLOW_RUN_ERROR(8040, "工作流运行失败 dify返回结果是 {0}"),


    GET_MODE_ERROR(8041, "模型信息获取失败,想要获取的模型id为 {0}"),

    WORK_FLOW_ERROR_MESSAGE(8042, "工作流运行失败 dify报错信息为: {0}"),

    DISPLAY_ENV_ERROR(8043, "环境变量展示异常 未找到最新环境变量更新记录 查找的环境变量名为 {0}"),

    SESSION_TIMEOUT(9009, "用户登录状态失效，请重新登录");

    private final int code;
    private final String message;

    BizResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
