package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum RedisHeadEnum {
    // 会话用户前缀
    PREFIX_USER_CHAT("ailab_user_chat"),
    // 问卷文件删除
    QUESTIONNAIRE("questionnaire"),
    // 用户difyToken
    DIFY_TOKEN_HEAD("dify_token:"),
    // 问卷文件暂定保存
    TEMP_QUESTIONNAIRE("tempQuestionnaire"),
    // 问卷星数据头部内容
    QUESTION_START_HEAD("question_start_head"),
    // 问卷星接收数据
    QUESTION_START_RECEPTION("question_start_reception"),
    // 保存历史聊天记录
    SAVE_CHAT_HISTORY("save_chat_history"),

    // 用户当前对话轮次
    USER_CHAT_COUNT("user_chat_count"),
    ;

    private String desc;

    RedisHeadEnum(String desc) {
        this.desc = desc;

    }
}
