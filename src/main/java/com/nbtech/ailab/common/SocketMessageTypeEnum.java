package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum SocketMessageTypeEnum {

    // 消息接收
    SEND_MESSAGE,

    // 修改实验室状态
    EDIT_ROOM,

    // 修改实验者状态
    EDIT_MEMBER,

    // 修改聊天室的模型自动发言状态
    AUTO_CHAT,

    // 心跳检测机制
    HEARTBREAK,

    // 切换聊天室使用状态
    CHANGE_OPEN_STATUS,

    // 用户登陆群发消息
    HANDSHAKE_NOTIFICATION,

    // 主持人发起工作流流程的使用
    USE_PROCESS,

    // 更新环境变量
    REFRESH_ENV,

    // 获取聊天室的环境变量
    GET_ROOM_ENV,

    // 回退工作流的使用记录
    ROLLBACK_WORKFLOW,

    // 主持人主动发起总结工作流调用
    CHAT_HOST_COMPLETE_WORKFLOW,

    // 某个用户完成所有的任务
    USER_OVER_WORK,

    // 用户聊天室发起使用工作流
    USER_RUN_WORKFLOW;

}
