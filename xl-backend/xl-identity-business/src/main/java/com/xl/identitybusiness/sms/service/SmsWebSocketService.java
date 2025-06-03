package com.xl.identitybusiness.sms.service;

import com.xl.common.dubbo.entity.SmsMessage;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/ws/{msisdn}")     // {msisdn}这里是客户端连接的时候需要输入msisdn才能连接
@Component
//@Component  Cannot deploy POJO class [com.xl.identitybusiness.sms.service.SmsService$$SpringCGLIB$$0] as it is not annotated with @ServerEndpoint
public class SmsWebSocketService {

    private static final Map<String, Session> clientSessions = new ConcurrentHashMap<>();
    private String msisdn;


    //当WebSocket连接成功建立时，由容器调用被注解的方法。
    @OnOpen
    public void onOpen(Session session, @PathParam("msisdn") String msisdn) throws IOException {
        this.msisdn = msisdn;
        clientSessions.put(msisdn, session);
        session.getBasicRemote().sendText("Connected as " + msisdn);
    }

    //当从WebSocket连接收到文本消息时，由容器调用被注解的方法
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText("ACK: " + message);
    }

    //当WebSocket连接被关闭时（无论是客户端主动关闭还是服务器端关闭），由容器调用被注解的方法
    @OnClose
    public void onClose(Session session) {
        clientSessions.remove(msisdn);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Error: {}", throwable.getMessage());
    }

    public static int sendMessage(SmsMessage message) {
        final int[] count = {0};
        if (message.getMsisdns() == null || message.getMsisdns().isEmpty()) {
            // 广播消息
            count[0] = broadcast(message.getContent());
        }else {
            // 点播
            message.getMsisdns().forEach(msisdn -> {
                count[0]++;
                sendTo(msisdn,message.getContent());});
        }
        return count[0];
    }
    /**
     * 广播消息
     */
    private static int broadcast(String message) {
        int count = 0;
        for (Map.Entry<String, Session> entry : getAllSessions().entrySet()) {
            try {
                entry.getValue().getBasicRemote().sendText("[Broadcast] " + message);
                count++;
            } catch (IOException e) {
                log.error("Failed to send SMS: {}", Arrays.toString(e.getStackTrace()));
            }
        }
        return count;
    }

    /**
     * 点播消息
     */
    private static boolean sendTo(String msisdn,String message) {
        Session session = getSession(msisdn);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText("[Direct] " + message);
                log.info("Sent to {}", msisdn);
                return true;
            } catch (IOException e) {
                log.error("Failed to send to {}: {}", msisdn, e.getMessage());
            }
        } else {
            log.error("Client not connected: {}", msisdn);
        }
        return false;
    }

    public static Session getSession(String clientId) {
        return clientSessions.get(clientId);
    }

    public static Map<String, Session> getAllSessions() {
        return clientSessions;
    }

}
