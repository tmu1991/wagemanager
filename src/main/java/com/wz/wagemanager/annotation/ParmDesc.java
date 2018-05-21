package com.wz.wagemanager.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ParmDesc {
    String desc() default "";
}
