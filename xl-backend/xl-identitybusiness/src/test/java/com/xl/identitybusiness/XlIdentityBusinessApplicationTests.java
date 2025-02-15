package com.xl.identitybusiness;

import com.xl.common.aspect.LogAspect;
import com.xl.identitybusiness.controller.Authentication;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {XlIdentityBusinessApplication.class})
class XlIdentityBusinessApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Test
    void contextLoads() {
        logger.info("sads");
    }

}
