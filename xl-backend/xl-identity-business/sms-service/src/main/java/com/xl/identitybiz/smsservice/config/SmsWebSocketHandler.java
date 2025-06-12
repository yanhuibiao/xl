//package com.xl.identitybusiness.sms.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Component
//public class SmsWebSocketHandler extends TextWebSocketHandler {
//
////    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//    private final ObjectMapper objectMapper = new ObjectMapper()
//            .registerModule(new JavaTimeModule());
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//        System.out.println("New session connected: " + session.getId());
//        System.out.println("Total sessions: " + sessions.size());
//        try {
//            session.sendMessage(new TextMessage("Welcome to the server!"));
//        } catch (IOException e) {
//            log.error("Failed to send WebSocket message: {}", e.getMessage());
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.remove(session);
//    }
//
//    public void sendMessage(Object message) {
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(message);
//            for (WebSocketSession session : sessions) {
//                if (session.isOpen()) {
//                    session.sendMessage(new TextMessage(jsonMessage));
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Failed to send WebSocket message: " + e.getMessage());
//        }
//    }
//}