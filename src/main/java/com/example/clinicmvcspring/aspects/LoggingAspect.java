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
                + " was called");

    }

    // no crashes
    @AfterReturning(pointcut = "execution(* com.example.clinicmvcspring.services.*.*(..))", returning = "result")
    public void logAfterSuccessfulReturn(JoinPoint joinPoint, Object result) {
        String paramString = getParametersAsString(joinPoint);
        String resultString;
        if (result == null) {
            // for logout (invalidateToken)
            resultString = "Void method (no return)";

        } else {
            resultString = result.toString();
        }

        log.info("Service method {" + joinPoint.getSignature().getName() + "} with the parameters("
                + paramString + ") from class " + joinPoint.getTarget().getClass().getSimpleName()
                + "finished successfully and returned [" + resultString + "]");

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

        try {
            return joinPoint.proceed();// press play (run the main method)
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("service method {" + joinPoint.getSignature().getName()
                    + "}" + " from class " + joinPoint.getTarget().getClass().getSimpleName() + "  started at("
                    + Instant.ofEpochMilli(startTime) + ") and finished at ("
                    + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                    + " ms)");
        }
    }

    @Around("execution(* com.example.clinicmvcspring.controllers.*.*(..))")
    public Object endpointExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();// press play (run the main method)

        } finally {

            long endTime = System.currentTimeMillis();

            long elapsedTime = endTime - startTime;

            log.info("controller endpoint {" + joinPoint.getSignature().getName()
                    + "}" + " from class " + joinPoint.getTarget().getClass().getSimpleName() + "  started at("
                    + Instant.ofEpochMilli(startTime) + ") and finished at ("
                    + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                    + " ms)");

        }
    }

    @Around("@annotation(com.example.clinicmvcspring.annotations.ExecutionTimeLog)")
    public Object methodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();// press play (run the main method)
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("method annotated with @ExecutionTimeLog {" + joinPoint.getSignature().getName()
                    + "}" + " from class " + joinPoint.getTarget().getClass().getSimpleName() + "  started at("
                    + Instant.ofEpochMilli(startTime) + ") and finished at ("
                    + Instant.ofEpochMilli(endTime) + ") time taken: (" + elapsedTime
                    + " ms)");
        }
    }

}
