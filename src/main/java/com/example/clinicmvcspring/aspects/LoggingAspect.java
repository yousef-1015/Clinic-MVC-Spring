package com.example.clinicmvcspring.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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

}
