package com.pgy.ups.common.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * ParamsLog注解记录入参和出参
 * @author 墨凉
 *
 */

@Aspect
@Component
public class ParamLogAspect implements Ordered{
	
	private Logger logger=LoggerFactory.getLogger(ParamLogAspect.class);
    
	//@ParamLog在方法上或类上 注解拦截
	@Pointcut("@annotation(com.pgy.ups.common.annotation.ParamsLog)||@within(com.pgy.ups.common.annotation.ParamsLog)")
	public void ParamLogPointcut() {}
	
	//记录入参回参日志
	@Around(value="ParamLogPointcut()")
	public Object dojob(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] params=joinPoint.getArgs();
		String methodName=joinPoint.getSignature().getName();
		String className=joinPoint.getSignature().getDeclaringTypeName();
		logger.info(className+"类"+methodName+"方法入参为：{}",Arrays.deepToString(params));
		Object obj=joinPoint.proceed();
		logger.info(className+"类"+methodName+"方法回参为：{}",obj);
		return obj;
	}
    
	/**
	 * 定义烂机器顺序
	 */
	@Override
	public int getOrder() {
		return 99;
	}
	
}
