package com.pgy.ups.common.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 方法执行时间
 * 
 * @author 墨凉
 *
 */

@Aspect
@Component
public class MethodExecuteTimeAspect implements Ordered {

	private Logger logger = LoggerFactory.getLogger(MethodExecuteTimeAspect.class);

	// @PrintExecuteTime在方法上注解拦截
	@Pointcut("@annotation(com.pgy.ups.common.annotation.PrintExecuteTime)")
	public void executeTimePointcut() {
	}

	// 记录方法执行时间
	@Around(value = "executeTimePointcut()")
	public Object executeTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature sig = joinPoint.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法！");
		}
		msig = (MethodSignature) sig;
		Object target = joinPoint.getTarget();
		Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		Long start = System.currentTimeMillis();
		try {
			return joinPoint.proceed();
		} finally {
			logger.info(target.getClass().getName() + "类" + currentMethod.getName() + "方法执行时间为："
					+ (System.currentTimeMillis() - start) + "毫秒！");
		}
	}

	/**
	 * 定义拦截器顺序
	 */
	@Override
	public int getOrder() {
		return 100;
	}

}
