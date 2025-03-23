package com.xl.common.config;

/**
 * mybatis打印sql需求使用这个类
 */
public class StdoutLogger extends com.p6spy.engine.spy.appender.StdoutLogger {
    public void logText(String text) {
        System.err.println(text);
    }
}