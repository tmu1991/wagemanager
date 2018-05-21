package com.wz.wagemanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author WindowsTen
 * @date 2017/10/27 16:59
 * @description
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.METHOD,ElementType.TYPE})
public @interface LoginVerification {
}
