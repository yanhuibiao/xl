package com.xl.smstool.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SmsMessage implements Serializable {
    private String messageId;
    private String content;
    private LocalDateTime sendTime;

    public SmsMessage() {}

    public SmsMessage(String messageId, String content, LocalDateTime sendTime) {
        this.messageId = messageId;
        this.content = content;
        this.sendTime = sendTime;
    }

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getSendTime() { return sendTime; }
    public void setSendTime(LocalDateTime sendTime) { this.sendTime = sendTime; }

    @Override
    public String toString() {
        return "SmsMessage{messageId='" + messageId + "', content='" + content + "', sendTime=" + sendTime + "}";
    }
}
