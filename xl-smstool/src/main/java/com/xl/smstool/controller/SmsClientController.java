package com.xl.smstool.controller;

import jakarta.annotation.PostConstruct;
import jakarta.websocket.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ClientEndpoint
public class SmsClientController {
    
    @FXML
    private TextField serverIpField;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField msisdnField;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea resultArea;
    @FXML
    private Button sendButton;
    @FXML
    private Button connectButton;
    private Session session;


//    @PostConstruct
    @FXML
    public void initialize() {
        sendButton.setDisable(true);
        serverIpField.setText("localhost");
        serverPortField.setText("18081");
        msisdnField.setText("10086");
    }

    @FXML
    public void connectWebSocket() {
        String ip = serverIpField.getText();
        String port = serverPortField.getText();
        String msisdn = msisdnField.getText().trim();
        if (ip.isEmpty() || port.isEmpty()||msisdn.isEmpty()) {
            appendMessage("请填写服务器 IP 和端口。");
            return;
        }
//        String uri = "ws://"+ip +":" +port + "/ib/ws/"+msisdn;
        String uri = String.format("ws://%s:%s/ib/ws/%s", ip, port, msisdn);

        if (session == null || !session.isOpen()) {

        }
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
            sendButton.setDisable(true);
            sendButton.setDisable(false);
            appendMessage("Connecting to server...");
        } catch (Exception e) {
            appendMessage("Connection failed: " + e.getMessage());
        }
    }

    @FXML
    public void sendMessage() {
        String message = messageField.getText();
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
            messageField.clear();
        } else {
            appendMessage("Not connected.");
        }
    }

    @FXML
    public void clearMessage() {
        resultArea.clear();
    }

    /**
     * 当WebSocket连接成功建立时，由容器调用被注解的方法。
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        appendMessage("Connected to server.");
    }

    /**
     * 当从WebSocket连接收到文本消息时，由容器调用被注解的方法
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        appendMessage("Received: " + message);
    }

    /**
     * 当WebSocket连接被关闭时（无论是客户端主动关闭还是服务器端关闭），由容器调用被注解的方法
     * @param session
     * @param reason
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        appendMessage("Connection closed: " + reason.getReasonPhrase());
        sendButton.setDisable(true);
        connectButton.setDisable(false);
        // 清除会话对象
        if (this.session != null) {
            this.session.close();
            this.session = null;
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        appendMessage("Error: " + throwable.getMessage());
    }

    private void appendMessage(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Platform.runLater(() -> resultArea.appendText("[" + time + "] Received: " + message + "\n"));
    }
}