package com.xl.common.exception;

import com.yanhuiby.common.dto.ResponseResult;
import com.yanhuiby.common.enums.ResponseCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //控制器增强类
@Log4j2
//@RestControllerAdvice = @ResponseBody+@ControllerAdvice
public class ExceptionCatch {
    /**
     *
     * @param be
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseResult exception(BusinessException be){
        be.printStackTrace();
        log.error("catch exception:{}",be.getMessage());
        return ResponseResult.errorResult(be.getResponseCodeEnum());
    }

    /**
     * 处理不可控异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        e.printStackTrace();
        log.error("catch exception:{}",e.getMessage());
        return ResponseResult.errorResult(ResponseCodeEnum.SERVER_ERROR);
    }
}
