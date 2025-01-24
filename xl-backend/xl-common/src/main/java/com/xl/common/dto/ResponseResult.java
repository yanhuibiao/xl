package com.xl.common.dto;


import com.yanhuiby.common.enums.ResponseCodeEnum;

import java.io.Serializable;

/**
 * 通用的结果返回类
 * @param <T>
 */
public class ResponseResult<T> implements Serializable {
    
    private Integer code;
    private String message;
    private T data;

    //构造器
    public ResponseResult() {
        this.code = 200;
        this.message = "success";
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    // ok结果
    public static ResponseResult<?> okResult(int code, String msg) {
        ResponseResult<?> result = new ResponseResult<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static ResponseResult<?> okResult(int code, Object data) {
        ResponseResult result = new ResponseResult<>();
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static ResponseResult<?> okResult(int code, String msg,Object data) {
        ResponseResult<?> result = okResult(code, data);
        result.setMessage(msg);
        return result;
    }

    public static ResponseResult<?> okResult(Object data) {
        ResponseResult result = okResult(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getmessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    // error结果
    public static ResponseResult<?> errorResult(int code, String msg) {
        ResponseResult<?> result = okResult(code,msg);
        return result;
    }
    
    public static ResponseResult<?> errorResult(ResponseCodeEnum enums){
        return new ResponseResult<>(enums.getCode(),enums.getmessage());
    }

    public static ResponseResult<?> errorResult(ResponseCodeEnum enums, String message){
        return new ResponseResult<>(enums.getCode(),message);
    }

    public static ResponseResult<?> errorResult(int code, ResponseCodeEnum enums){
        return new ResponseResult<>(code,enums.getmessage());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
