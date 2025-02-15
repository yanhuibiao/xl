package com.xl.test.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.Generator;
import com.xl.common.utils.NacosConfigUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class CommonTest {

    @NacosValue(value = "${spring.data.redis.host}",autoRefreshed = true)
    String redishost;

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

    @Test
    void test3(){
        System.out.println(NacosConfigUtil.getYamlConfig("redis","DEFAULT_GROUP","spring.data.redis.host"));
    }

    @Test
    void test4(){
        NacosConfigUtil a = new NacosConfigUtil("redis", "DEFAULT_GROUP","spring.data.redis.host");
        a.addYamlListener();
        // 保持程序运行以监听配置变化
        while (true) {
            try {
                Thread.sleep(3000);
                System.out.println(a.propertiesValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void test5(){
        System.out.println(redishost);
    }

}
