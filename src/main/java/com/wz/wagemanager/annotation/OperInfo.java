package com.wz.wagemanager.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OperInfo {
    //操作描述，使用SPEL表达式
    String desc() default "";
    OperationType type();
}
