package com.nbtech.ailab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplate() {
        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 配置请求工厂（可选）
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 连接超时时间
        factory.setReadTimeout(5000);     // 读取超时时间
        restTemplate.setRequestFactory(factory);

        // 配置消息转换器（可选）
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        return restTemplate;
    }
}
