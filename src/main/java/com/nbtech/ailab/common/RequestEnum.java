package com.nbtech.ailab.common;

import lombok.Getter;

@Getter
public enum RequestEnum {

    // 复制工作流
    COPY_WORKFLOW("/console/api/apps/%s/msy/copy"),

    // 查询工作流算子的开始参数和结束参数集合
    WORKFLOW_START_END("/console/api/apps/%s/msy/workflows/draft"),

    // dify原生 新增基础模型
    DIFY_BASIC_MODEL("/console/api/workspaces/current/model-providers/xinference/models"),

    // 启用/禁用基础模型
    ENABLE_BASIC_MODEL("/console/api/workspaces/current/model-providers/msy/models/status"),

    // 删除基础模型
    DELETED_BASIC_MODEL("/console/api/workspaces/current/model-providers/msytongyi"),

    // 新增基础模型
    ADD_BASIC_MODEL("/console/api/workspaces/current/model-providers/msytongyi"),

    // url解析获取后缀和文件大小
    RUN_WORK_FLOW("/console/api/apps"),

    // url解析获取后缀和文件大小
    FORMAT_URL("/console/api/remote-files/"),

    // workFlow文件上传接口
    UPDATE_WORKFLOW("/console/api/apps"),

    // workFlow文件上传接口
    WORK_FLOW_FILE_UPLOAD("/console/api/files/upload"),

    // api运行workFlow的Api地址
    API_RUN_WORK_FLOW("/v1/workflows/run"),

    // 获取一个app的apikey集合
    WORK_FLOW_DRAFT("/console/api/apps"),

    // 获取一个工作流的详情
    GET_WORK_FLOW_DETAIL("/console/api/apps"),

    // 获取一个app的apikey集合
    GET_API_KEYS("/console/api/apps"),

    // 删除一个工作流
    DELETED_WORK_FLOW("/console/api/apps"),

    // 新增一个工作流
    ADD_WORK_FLOW("/console/api/apps"),

    // list查询工作流
    WORK_FLOW_LIST("/console/api/apps/clear"),

    // 分页查询工作流
    WORK_FLOW_PAGE("/console/api/apps"),

    // 用户登出
    LOGOUT_USER("/console/api/logout"),

    // 用户注册
    REGISTER_USER("/console/api/setup"),

    // Dify的login接口
    LOGIN_PATH("/console/api/login");

    private final String url;

    RequestEnum(String url) {
        this.url = url;
    }
}
