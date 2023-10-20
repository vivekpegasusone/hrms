package com.whizzy.hrms.core.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.UNKNOWN;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("com.whizzy.hrms.core.logging.LoggingPointCuts.pointcutOnAnnotationAndNonLoggable()")
    public void logBefore(JoinPoint joinPoint) throws Throwable {
        log.info("Inside {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @Around("com.whizzy.hrms.core.logging.LoggingPointCuts.pointcutBasedLoggableAnnotation()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Entering {}.{}() with argument[s] {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        Object output = joinPoint.proceed();
        log.info("Exiting {}.{}() with argument[s] {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        return output;
    }

    @AfterThrowing(pointcut = "com.whizzy.hrms.core.logging.LoggingPointCuts.pointcutBasedOnPackage()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Objects.nonNull(e.getCause()) ? e.getCause() : UNKNOWN);
    }
}