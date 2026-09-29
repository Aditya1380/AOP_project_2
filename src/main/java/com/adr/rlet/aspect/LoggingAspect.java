package com.adr.rlet.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Pointcut("execution(* com.adr.rlet.service..*(..))")
	public void serviceMethods() {};
	
	
	@Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
		
        log.info("➡️  BEFORE: {} called with args = {}",joinPoint.getSignature().toShortString(),Arrays.toString(joinPoint.getArgs()));
    }
	
	@AfterReturning(
            pointcut = "serviceMethods()",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("✅ AFTER RETURNING: {} returned = {}",
                joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(
            pointcut = "serviceMethods()",
            throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("❌ AFTER THROWING: {} threw = {}",
                joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("🔚 AFTER (finally): {} finished",
                joinPoint.getSignature().toShortString());        
    }
    
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("⏱️  AROUND (before): {} starting",
                pjp.getSignature().toShortString());

        try {
            Object result = pjp.proceed();   // <-- this is what actually calls the real method
            long duration = System.currentTimeMillis() - start;
            log.info("⏱️  AROUND (after): {} completed in {} ms, result = {}",
                    pjp.getSignature().toShortString(), duration, result);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("⏱️  AROUND (error): {} failed after {} ms, error = {}",
                    pjp.getSignature().toShortString(), duration, ex.getMessage());
            throw ex;   // <-- important: rethrow, don't swallow
        }
    }
}
