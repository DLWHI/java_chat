package com.dlwhi.client.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO add compile time argument checking for binds
// TODO or find a way to remove binds
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Binded {
    String command();
    String[] parameterNames() default {};
}
