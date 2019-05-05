package com.pgy.ups.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.pgy.ups.common.annotation.RedisLock;
import com.pgy.ups.common.exception.RedisLockFailException;
import com.pgy.ups.common.utils.RedisUtils;

/**
 * RedisLock
 * 
 * @author 墨凉
 *
 */

@Aspect
@Component
public class RedisLockAspect implements Ordered {

	private Logger logger = LoggerFactory.getLogger(RedisLockAspect.class);
	
	@Resource
	private RedisUtils redisUtils;

	// @RedisLock在方法上或类上 注解拦截
	@Pointcut("@annotation(com.pgy.ups.common.annotation.RedisLock)")
	public void redisLockPointcut() {
	}

	// redis分布式锁
	@Around(value = "redisLockPointcut()")
	public Object lockAndUnlock(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature sig = joinPoint.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法！");
		}
		msig = (MethodSignature) sig;
		Object target = joinPoint.getTarget();
		Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		Annotation annotation = currentMethod.getAnnotation(RedisLock.class);
		// 获取注解属性
		String key = null;
		String value = null;
		String name = null;
		try {
			key = (String) AnnotationUtils.getValue(annotation, "key");
			value = UUID.randomUUID().toString();
			int expireTime = (int) AnnotationUtils.getValue(annotation, "expireTime");
			name = (String) AnnotationUtils.getValue(annotation, "name");
			if (redisUtils.redisLock(key, value, expireTime)) {
				logger.info("线程：{} 获取redis锁成功，lockKey：{}, lockValue：{} ,lockName：{}", Thread.currentThread().getId(),
						key, value, name);
				return joinPoint.proceed();
			} else {
				logger.info("线程：{} 获取redis锁失败，lockKey：{}, lockValue：{} ,lockName：{}", Thread.currentThread().getId(),
						key, value, name);
				throw new RedisLockFailException();
			}

		} finally {
			if (redisUtils.redisUnLock(key, value)) {
				logger.info("线程：{} 解除redis锁成功，lockKey：{}, lockValue：{} ,lockName：{}", Thread.currentThread().getId(),
						key, value, name);
			} else {
				logger.info("线程：{} 解除redis锁失败，lockKey：{}, lockValue：{} ,lockName：{}", Thread.currentThread().getId(),
						key, value, name);
			}
		}
	}

	/**
	 * 定义拦截器顺序
	 */
	@Override
	public int getOrder() {
		return 1;
	}

}
