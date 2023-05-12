package com.api.central.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
/* Copyright 2016 BSOL Systems- IVMASNG To Present - All rights reserved*/

  
@Aspect
@Component
public class AppLogger {
	private static final Logger logger =  LoggerFactory.getLogger(AppLogger.class);	
	@Around("execution(* com.api.central.*.*.*.*(..))")
	public Object modelBeanAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.currentTimeMillis();
		long elapsedTime = 0 ;
		try {
			System.out.println("Hello");
			elapsedTime = System.currentTimeMillis() - start;
			logger.info("Method " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " ()" + " Request Data : "
					+ org.apache.commons.lang.ArrayUtils.toString(joinPoint.getArgs()).toString() );
			return joinPoint.proceed();
		}catch (Exception e) {
			logger.error("Exception FullStackTrace :" + org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e));
			throw e;
		}finally{
		   // logger.info("Execution time : "+elapsedTime+" ms");

		}
	}
}
