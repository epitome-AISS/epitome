package com.nbtech.ailab.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.nbtech.ailab.util.MinioUtil;
import com.nbtech.ailab.util.RedisService;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig {

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(2 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(2 * 1024 * 1024);
        long timeout = 2 * 60 * 60 * 1000L;
        container.setMaxSessionIdleTimeout(timeout);
        log.info("WebSocket容器超时时间设置为: {} 毫秒 ({} 小时)", timeout, timeout / (60 * 60 * 1000));
        return container;
    }

    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        WebSocketServer.redisService = redisService;
    }


    @Autowired
    public void setAddress(ChatRoomProperties chatRoomProperties) {
        WebSocketServer.address = chatRoomProperties.getAddress();
    }


    @Autowired
    public void setMinioUtil(MinioUtil minioUtil) {
        WebSocketServer.minioUtil = minioUtil;
    }

}
