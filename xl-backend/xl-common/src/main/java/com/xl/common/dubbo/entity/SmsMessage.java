package com.xl.common.dubbo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

//短信消息实体类
@Data
public class SmsMessage implements Serializable {
    private String messageId;
    private String content;
    private LocalDateTime sendTime;
    private String status;
    private List<String> msisdns;

    public SmsMessage() {}

    public SmsMessage(String messageId, String content, LocalDateTime sendTime,List<String> msisdns) {
        this.messageId = messageId;
        this.content = content;
        this.sendTime = sendTime;
        this.msisdns = msisdns;
    }

    @Override
    public String toString() {
        return "SmsMessage{messageId='" + messageId + "', content='" + content + "', sendTime=" + sendTime + "}";
    }

    public static class SmsMessageStatus{
        public String SUCCESS = "success";
        public String FAILURE = "failure";
    }
}