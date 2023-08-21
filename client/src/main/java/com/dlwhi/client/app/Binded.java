package com.dlwhi.client.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dlwhi.client.app.App.Command;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Binded {
    Command command();
    String[] parameterNames() default {};
}
