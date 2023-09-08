package com.dlwhi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


// Wrapper class to call method of object
public class Call {
    private final Object target;
    private final Method method;
    private final String[] paramNames;

    public Call(Object target, Method method) {
        this.target = target;
        this.method = method;
        method.setAccessible(true);
        Parameter[] params = method.getParameters();
        paramNames = new String[method.getParameterCount()];
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = params[i].getName();
        }
    }

    // Tries to wrap method named methodNa of object target
    // Returns null on any error    
    public static Call extract(Object target, String methodName) {
        Method method = null;
        try {
            method = target.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            System.err.println(e);
            System.err.printf(
                "Class %s has no method %s%n",
                target.getClass().getSimpleName(),
                methodName
            );
        } catch (SecurityException e) {
            System.err.println(e);
            System.err.printf(
                "Invalid access on method %s of class %s%n",
                methodName,
                target.getClass().getSimpleName()
            );
        }
        return (method == null) ? null : new Call(target, method);
    }

    public void invoke(Object... args) {
        try {
            method.invoke(target, args);
        } catch (IllegalAccessException e) {
            System.err.println(e);
            System.err.printf(
                "Invalid access on method %s of class %s%n",
                method.getName(),
                target.getClass().getSimpleName()
            );
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.err.printf(
                "Invalid argument provided to method %s of class %s%n",
                method.getName(),
                target.getClass().getSimpleName()
            );
        } catch (InvocationTargetException e) {
            System.err.printf(
                "%s threw an exception of type %n",
                method.getName(),
                e.getTargetException().getClass().getName()
            );
        } 
    }

    // Return array to store parameters
    public Object[] getParameterArray() {
        return new Object[method.getParameterCount()];
    }

    public void setParamaterName(int index, String name) {
        paramNames[index] = name;
    }

    public String getParameterName(int index) {
        return paramNames[index];
    }
}
