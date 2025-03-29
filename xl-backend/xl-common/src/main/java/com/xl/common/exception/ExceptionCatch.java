package com.xl.common.exception;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.enums.ResponseCodeEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //控制器增强类
@Slf4j
//@RestControllerAdvice = @ResponseBody+@ControllerAdvice
public class ExceptionCatch {
    /**
     *
     * @param be
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity exception(BusinessException be){
        be.printStackTrace();
        if (be.getResponseCodeEnum() == null){
            log.error(be.getMsg());
            return ResponseEntity.errorResult(be.getCode(),be.getMsg());
        }else {
            log.error("catch exception:{}",be.getResponseCodeEnum().getmessage());
            return ResponseEntity.errorResult(be.getResponseCodeEnum());
        }
    }

    /**
     * 处理不可控异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity exception(Exception e){
        e.printStackTrace();
        log.error("catch exception:{}",e.getMessage());
        return ResponseEntity.errorResult(ResponseCodeEnum.SERVER_ERROR, String.valueOf(e.getCause()).replace("\"","'"));
    }
}
