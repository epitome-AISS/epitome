package com.nbtech.ailab.constant;

/**
 * 所有的外部url路径
 */
public interface HttpUrlRecord {
    /**
     * 创建聊天室的路径
     */
    String OPEN_CHAT_ROOM = "/init_chatroom";

    /**
     * 发送聊天消息
     */
    String CHAT = "/chat";

    /**
     * 销毁聊天室
     */
    String DESTROY_CHAT_ROOM = "/destroy_chatroom";

    /**
     * 销毁聊天室
     */
    String CHANGE_CHATROOM_CONFIG = "/change_chatroom_config";

    /**
     * 验证基础模型是否可用
     */
    String VERIFY_MODEL = "/verify_llm_model";
}
