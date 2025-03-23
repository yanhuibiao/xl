package com.xl.common.exception;

import com.xl.common.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BusinessException extends RuntimeException {


    public ResponseCodeEnum responseCodeEnum;
    int code;
    String msg;

//    无参构造器
    public BusinessException(){
    }

    public BusinessException(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ResponseCodeEnum responseCodeEnum){
        this.responseCodeEnum = responseCodeEnum;
    }

}
