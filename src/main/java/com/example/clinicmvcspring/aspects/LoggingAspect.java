package com.example.clinicmvcspring.aspects;

import java.time.Instant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Advice
    @Before("execution(* com.example.clinicmvcspring.services.*.*(..))")
    public void beginLogging(JoinPoint joinPoint) {
        log.info("Service method " + joinPoint.getSignature().getName() + " was called");

    }

    // no crashes
    @AfterReturning(pointcut = "execution(* com.example.clinicmvcspring.services.*.*(..))", returning = "result")
    public void logAfterSuccessfulReturn(JoinPoint joinPoint, Object result) {

        log.info("Service method {" + joinPoint.getSignature().getName()
                + "} finished successfully and returned:\n Data {" + result.toString() + "}");

    }

    // exception where thrown
    @AfterThrowing(pointcut = "execution(* com.example.clinicmvcspring.services.*.*(..))", throwing = "error")
    public void LogAfterExceptions(JoinPoint joinPoint, Exception error) {
        log.error("Service method {" + joinPoint.getSignature().getName()
                + "} Threw an Exception:[" + error.getMessage() + "]");

    }

    // @Around is a middle man not observer like @Before,After

    @Around("execution(* com.example.clinicmvcspring.services.*.*(..))")
    public Object serviceMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();// press play (run the main method)

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        log.info("Service method {" + joinPoint.getSignature().getName()
                + "} started at(" + Instant.ofEpochMilli(startTime) + ") and finished at ("
                + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                + " ms)");

        return result;

    }

    @Around("execution(* com.example.clinicmvcspring.controllers.*.*(..))")
    public Object endpointExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();// press play (run the main method)

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        log.info("controller endpoint {" + joinPoint.getSignature().getName()
                + "} started at(" + Instant.ofEpochMilli(startTime) + ") and finished at ("
                + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                + " ms)");

        return result;

    }

}
