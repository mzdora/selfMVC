package com.mzdora.bean;

import com.mzdora.aop.advice.AroundAdvice;
import com.mzdora.aop.annotation.Aspect;
import com.mzdora.core.annotation.Controller;

import java.lang.reflect.Method;

@Aspect(pointcut = "execution(* com.mzdora.bean.mzController.helloForAspect(..))")
public class mzAspect implements AroundAdvice {
    @Override
    public void afterReturning(Class<?> clz, Object returnValue, Method method, Object[] args) throws Throwable {
        System.out.println("after");
    }

    @Override
    public void before(Class<?> clz, Method method, Object[] args) throws Throwable {
        System.out.println("before");
    }

    @Override
    public void afterThrowing(Class<?> clz, Method method, Object[] args, Throwable e) {
        System.out.println("throw");
    }
}
