package com.example.clinicmvcspring.aspects;

import org.aspectj.lang.JoinPoint;
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

}
