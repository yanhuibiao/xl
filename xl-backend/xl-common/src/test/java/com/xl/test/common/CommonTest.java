package com.xl.test.common;

import com.alibaba.fastjson.JSON;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.Generator;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class CommonTest {

    @Test
    void test1() throws IOException {
        System.out.println(Generator.generatorPin(6));
    }

    @Test
    void test2(){
        BusinessException businessException;
        businessException = new BusinessException(ResponseCodeEnum.LOGIN_FAILED);
        System.out.println(JSON.toJSONString(businessException));
    }

}
