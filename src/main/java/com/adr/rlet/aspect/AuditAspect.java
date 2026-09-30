package com.adr.rlet.aspect;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.adr.rlet.audit.Audited;

@Aspect
@Component
public class AuditAspect {

	private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

	@Around("@annotation(audited)")
	public Object audit(ProceedingJoinPoint pjp, Audited audited) throws Throwable {

		String action = audited.action();
		String method = pjp.getSignature().toShortString();
		String args = Arrays.toString(pjp.getArgs());
		LocalDateTime timestamp = LocalDateTime.now();

		try {
            Object result = pjp.proceed();

            log.info("📝 AUDIT | action={} | method={} | args={} | status=SUCCESS | result={} | time={}",
                    action, method, args, result, timestamp);

            return result;
        } catch (Throwable ex) {
            log.error("📝 AUDIT | action={} | method={} | args={} | status=FAILED | error={} | time={}",
                    action, method, args, ex.getMessage(), timestamp);
            throw ex;
        }
	}

}
