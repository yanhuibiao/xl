package com.xl.common.exception;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ExceptionFilter;
import org.apache.dubbo.rpc.service.GenericService;

//Activate group属性指定此类仅对于服务提供者生效, order属性:指定filter执行优先级,越小,执行越靠前;
@Activate(order = -30000)
public class CustomeDubboExceptionFilter extends ExceptionFilter {

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            Throwable exception = appResponse.getException();
            if (exception instanceof RuntimeException) {
                return;
            }
            super.onResponse(appResponse, invoker, invocation);
        }
    }
}
