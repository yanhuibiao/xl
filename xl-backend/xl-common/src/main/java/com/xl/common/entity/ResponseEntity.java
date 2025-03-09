package com.xl.common.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.xl.common.enums.ResponseCodeEnum;

import java.io.Serializable;

/**
 * 通用的结果返回类
 * @param <T>
 */
public class ResponseEntity<T> implements Serializable {
    
    private Integer code;
    private String message;
//    @JsonInclude(JsonInclude.Include.NON_NULL)  //为空则不序列化
    private T data = (T) "";

    //构造器
    public ResponseEntity() {
        this.code = 200;
        this.message = "success";
    }

    public ResponseEntity(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseEntity(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseEntity(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    // ok结果
    public static ResponseEntity<?> okResult(int code, String msg) {
        ResponseEntity<?> result = new ResponseEntity<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static ResponseEntity<?> okResult(int code, Object data) {
        ResponseEntity result = new ResponseEntity<>();
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static ResponseEntity<?> okResult(int code, String msg, Object data) {
        ResponseEntity<?> result = okResult(code, data);
        result.setMessage(msg);
        return result;
    }

    public static ResponseEntity<?> okResult(Object data) {
        ResponseEntity result = okResult(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getmessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    // error结果
    public static ResponseEntity<?> errorResult(int code, String msg) {
        ResponseEntity<?> result = okResult(code,msg);
        return result;
    }
    
    public static ResponseEntity<?> errorResult(ResponseCodeEnum enums){
        return new ResponseEntity<>(enums.getCode(),enums.getmessage());
    }

    public static ResponseEntity<?> errorResult(ResponseCodeEnum enums, String message){
        return new ResponseEntity<>(enums.getCode(),message);
    }

    public static ResponseEntity<?> errorResult(int code, ResponseCodeEnum enums){
        return new ResponseEntity<>(code,enums.getmessage());
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
