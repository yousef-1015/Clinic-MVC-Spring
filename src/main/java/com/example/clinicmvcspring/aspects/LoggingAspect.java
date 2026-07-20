package com.example.clinicmvcspring.aspects;

import java.lang.reflect.Array;
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
    private String getParametersAsString(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        return java.util.Arrays.toString(params);
    }

    // Advice
    @Before("execution(* com.example.clinicmvcspring.services.*.*(..))")
    public void beginLogging(JoinPoint joinPoint) {
        String paramString = getParametersAsString(joinPoint);

        log.info("Service method {" + joinPoint.getSignature().getName() + "} with the parameters("
                + paramString + ") from class " + joinPoint.getTarget().getClass().getSimpleName()
                + "was called");

    }

    // no crashes
    @AfterReturning(pointcut = "execution(* com.example.clinicmvcspring.services.*.*(..))", returning = "result")
    public void logAfterSuccessfulReturn(JoinPoint joinPoint, Object result) {
        String paramString = getParametersAsString(joinPoint);

        log.info("Service method {" + joinPoint.getSignature().getName() + "} with the parameters("
                + paramString + ") from class " + joinPoint.getTarget().getClass().getSimpleName()
                + "finished successfully and returned [" + result.toString() + "]");

    }

    // exception where thrown
    @AfterThrowing(pointcut = "execution(* com.example.clinicmvcspring.services.*.*(..))", throwing = "error")
    public void LogAfterExceptions(JoinPoint joinPoint, Exception error) {
        String paramString = getParametersAsString(joinPoint);

        log.error("Service method {" + joinPoint.getSignature().getName() + "} with the parameters("
                + paramString + ") from class " + joinPoint.getTarget().getClass().getSimpleName()
                + " threw an exception: {" + error.getMessage() + "}");

    }

    // @Around is a middle man not observer like @Before,After

    @Around("execution(* com.example.clinicmvcspring.services.*.*(..))")
    public Object serviceMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();// press play (run the main method)

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        log.info("Service method {" + joinPoint.getSignature().getName()
                + "}" + " from class " + joinPoint.getTarget().getClass().getSimpleName() + " started at("
                + Instant.ofEpochMilli(startTime) + ") and finished at ("
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
                + "}" + " from class " + joinPoint.getTarget().getClass().getSimpleName() + "  started at("
                + Instant.ofEpochMilli(startTime) + ") and finished at ("
                + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                + " ms)");

        return result;

    }

}
