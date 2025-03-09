package com.xl.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义注解，标记需要记录日志的方法
@Target(ElementType.TYPE)  //方法级别的注解
@Retention(RetentionPolicy.RUNTIME)
public @interface NotLogAnnotation {
    String value() default "NotLogAnnotation default value";
}
