package in.fourbits.etracker.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EtrackerAspect {
	
	public static final Logger logger = Logger.getLogger(EtrackerAspect.class);
	
	@Pointcut("execution(* in.fourbits.etracker.service.*.*(..))")
	public void pointcut() {
		
	}
	
	//@Before("pointcut()")	// This is also possible	 
	@Before("execution(* in.fourbits.etracker.service.*.*(..))") 
	public void pointcutBefore(JoinPoint joinPoint) {
		logger.info("Method "+joinPoint.getSignature()+" was called with parameters "+joinPoint.getArgs()[0]);
	}
	
	/*@Around("execution(* in.fourbits.etracker.service.*.*(..))")
	public void pointcutAround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("@Around: Before calculation-"+ new Date());
		joinPoint.proceed();
		System.out.println("@Around: After calculation-"+ new Date());
	}*/
	
	@AfterReturning(pointcut = "execution(* in.fourbits.etracker.service.*.*(..))",
			returning="val")
	public void logAfterReturning(Object val){
		System.out.println("Method return value:"+ val);
		System.out.println("@AfterReturning:"+new Date());
	}

}
