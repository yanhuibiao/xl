package com.xl.common.dubbo.api;


public interface SmsServiceTool {

    void broadcast(String message);

    void sendTo(String msisdn, String message);
}
