package com.nbtech.ailab.websocket;



import com.nbtech.ailab.common.*;
import com.nbtech.ailab.util.MinioUtil;
import com.nbtech.ailab.util.RedisService;
import com.nbtech.ailab.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author van
 */
@ServerEndpoint("/websocketChatRoom/{elementId}/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    public static Map<String, Map<Long, Session>> roomMap = new ConcurrentHashMap<>();

    public static RedisService redisService;

    public static String address;

    public static MinioUtil minioUtil;

    /**
     * 发送消息
     */
    public void sendMessage(String elementId, Long userId, String message) {
        Map<Long, Session> sessionMap = roomMap.get(elementId);
        if (sessionMap == null) {
            log.warn("聊天室 {} 不存在", elementId);
            return;
        }

        Session session = sessionMap.get(userId);
        if (session != null) {
            synchronized (session) {
                if (session.isOpen()) {
                    if (session.getAsyncRemote() == null) {
                        log.error("getAsyncRemote is null, message{}", message);
                        return;
                    }
                    try {
                        session.getAsyncRemote().sendText(message);
                    } catch (Exception e) {
                        log.error("发送消息失败: elementId={}, userId={}, error={}", elementId, userId, e.getMessage());
                    }
                } else {
                    log.error("session is not open: id={}", session.getId());
                }
            }
        } else {
            log.warn("用户 {} 在聊天室 {} 中不存在", userId, elementId);
        }
    }

    /**
     * 群发消息
     *
     * @param elementId
     * @param message
     */
    public void fanoutMessage(String elementId, String message) {
        Map<Long, Session> sessionMap = roomMap.get(elementId);
        if (sessionMap == null) {
            log.warn("聊天室 {} 不存在，无法群发消息", elementId);
            return;
        }

        // 创建用户ID的副本，避免并发修改异常
        Set<Long> userIds = new HashSet<>(sessionMap.keySet());
        userIds.forEach(userId -> sendMessage(elementId, userId, message));
    }

    /**
     * 建立连接成功调用
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("elementId") String elementId, @PathParam("userId") Long userId)
            throws IOException {
        log.info("{}加入webSocket！", userId);
        log.info("本次连接的 算子id信息={}, 用户id信息={}", elementId, userId);
        // 设置session超时时间
        long timeout = 2 * 60 * 60 * 1000L;
        session.setMaxIdleTimeout(timeout);
        log.info("Session超时时间设置为: {} 毫秒 ({} 小时), Session ID: {}", 
                 timeout, timeout / (60 * 60 * 1000), session.getId());

        Map<Long, Session> sessionMap = new ConcurrentHashMap<>();
        // 通过用户id找到对应的算子id 每个聊天室算子对应 一个聊天室
        if (roomMap.containsKey(elementId)) {
            sessionMap = roomMap.get(elementId);
            sessionMap.put(userId, session);
        } else {
            sessionMap.put(userId, session);
            roomMap.put(elementId, sessionMap);
        }

        roomMap.forEach((roomId1, sessionMap1) -> {
            sessionMap1.forEach((userId1, session1) -> {
            });
        });
    }

    /**
     * 关闭连接时调用
     *
     * @param session
     * @param elementId
     * @param userId
     */
    @OnClose
    public void onClose(Session session, @PathParam("elementId") String elementId, @PathParam("userId") Long userId)
            throws Exception {
        UserOpenStatusVo openStatusVo = new UserOpenStatusVo();
        SocketMessageVo socketMessageVo = new SocketMessageVo();
        openStatusVo.setKeepStatus(SocketAliveEnum.OFF_LINE.name());
        openStatusVo.setUserId(userId);
        openStatusVo.setElementId(elementId);
        socketMessageVo.setType(SocketMessageTypeEnum.HANDSHAKE_NOTIFICATION.name());
        socketMessageVo.setState(openStatusVo);
        // session不用手动关闭 30秒到时会自动关闭
        log.info("websocket连接关闭后回调 {} {}", userId, elementId);
    }

    /**
     * 收到客户端信息
     *
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) throws Exception {
        String info = "客户端：" + message + ",已收到";
        log.info(info);
    }

    /**
     * 错误时调用
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("On Error.", throwable);
    }




    /**
     * 心跳检测
     */
    private void hearBreak(String message, HeartBreakVo heartBreakVo) {
        if (heartBreakVo.getUserStatus().equals(SocketAliveEnum.ALIVE.name())) {
            String elementId = heartBreakVo.getElementId();
            Long userId = heartBreakVo.getUserId();
            // 获取用户的session并刷新超时时间
            Map<Long, Session> sessionMap = roomMap.get(elementId);
            if (sessionMap != null) {
                Session session = sessionMap.get(userId);
                if (session != null && session.isOpen()) {
                    // 重新设置session超时时间为30秒
                    session.setMaxIdleTimeout(30000);
                    // 给心跳发送方发送消息检测心跳
                    sendMessage(elementId, userId, message);
                }
            }
        }
    }
}