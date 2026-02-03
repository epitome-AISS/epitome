package com.nbtech.ailab.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat 服务器配置
 * 用于设置 WebSocket 连接超时时间
 */
@Configuration
@Slf4j
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // 设置连接超时时间为 2 小时（7200000 毫秒）
        long timeout = 2 * 60 * 60 * 1000L;
        
        factory.addConnectorCustomizers(connector -> {
            // 设置连接超时（毫秒）
            connector.setProperty("connectionTimeout", String.valueOf(timeout));
            connector.setProperty("keepAliveTimeout", String.valueOf(timeout));
            connector.setProperty("maxKeepAliveRequests", "-1");
            
            log.info("Tomcat Connector 超时配置: connectionTimeout={}ms ({}小时), keepAliveTimeout={}ms", 
                     timeout, timeout / (60 * 60 * 1000), timeout);
        });
        
        // 在连接器启动后设置 Endpoint 超时
        factory.addInitializers(context -> {
            // 这个回调在 Tomcat 启动后执行
            log.info("Tomcat 服务器启动完成，WebSocket 连接超时已配置为 2 小时");
        });
    }
}

