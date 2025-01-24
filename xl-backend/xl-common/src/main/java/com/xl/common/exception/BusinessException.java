package com.xl.common.exception;

import com.xl.common.enums.ResponseCodeEnum;

public class BusinessException extends RuntimeException {

    public ResponseCodeEnum responseCodeEnum;

//    无参构造器
    public BusinessException(){
    }

    public BusinessException(ResponseCodeEnum responseCodeEnum){
        this.responseCodeEnum = responseCodeEnum;
    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return responseCodeEnum;
    }

    public void setResponseCodeEnum(ResponseCodeEnum responseCodeEnum) {
        this.responseCodeEnum = responseCodeEnum;
    }
}
