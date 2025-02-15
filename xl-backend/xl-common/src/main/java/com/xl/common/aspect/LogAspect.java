package com.xl.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // 方法执行前
    @Before("execution(* com.xl..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Method {} is about to be called with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    // 方法执行后
    @After("execution(* com.xl..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Method {} has been execution completed.", joinPoint.getSignature());
    }

    // 方法执行返回后
    @AfterReturning(pointcut = "execution(* com.xl..*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method {} executed successfully with return value: {}", joinPoint.getSignature(), result);
    }

    // 方法执行出现异常时
    @AfterThrowing(pointcut = "execution(* com.xl..*.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Method {} executed with an exception: {}", joinPoint.getSignature(), ex.getMessage());
    }


    // 定义切点：匹配所有标注了@Loggable注解的方法
    @Pointcut("@annotation(com.xl.common.annotation.LogAnnotation)")
    public void myCustomAnnotationPointcut() {}

    @Around("myCustomAnnotationPointcut()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("Method {} executing time: {}ms",pjp.getSignature(),(endTime-startTime));
        return result;
    }
}
