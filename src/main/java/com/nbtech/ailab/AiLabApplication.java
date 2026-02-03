package com.nbtech.ailab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan(value = {"com.nbtech.ailab.*.dao"})
@EnableWebSocket
@EnableAsync
public class AiLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiLabApplication.class, args);
    }

}
