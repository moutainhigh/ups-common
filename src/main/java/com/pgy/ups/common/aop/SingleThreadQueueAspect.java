package com.pgy.ups.common.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

import com.pgy.ups.common.annotation.SingleThreadQueue;

/**
 * 单线程处理任务队列
 * 
 * @author 墨凉
 *
 */

@Aspect
@Component
public class SingleThreadQueueAspect implements Ordered {

	private Logger logger = LoggerFactory.getLogger(SingleThreadQueueAspect.class);

	/**
	 * 创建单线程池
	 */
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	// @SingleThreadQueue在方法上注解拦截
	@Pointcut("@annotation(com.pgy.ups.common.annotation.SingleThreadQueue)")
	public void SingleTreadPoolQueuePointcut() {
	}

	@Around(value = "SingleTreadPoolQueuePointcut()")
	public Object executeInSingleTreadPool(ProceedingJoinPoint joinPoint) throws Throwable {

		Signature sig = joinPoint.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法！");
		}
		msig = (MethodSignature) sig;
		Object target = joinPoint.getTarget();
		Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		// 获取SingleThreadQueue注解
		Annotation annotation = currentMethod.getAnnotation(SingleThreadQueue.class);
		// 获取注解属性
		String name = (String) AnnotationUtils.getValue(annotation, "name");
		int timeout = (int) AnnotationUtils.getValue(annotation, "timeout");

		// 创建任务对象
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					logger.info("SingleThreadQueue任务执行开始，name:{}，设置超时时间为{}毫秒！", name, timeout);
					return joinPoint.proceed();
				} catch (Throwable e) {
					throw new Exception(e);
				} finally {
					logger.info("SingleThreadQueue任务执行结束！name:{}", name);
				}
			}
		};
		// 任务放入线程池中
		Future<Object> future = executorService.submit(callable);
		try {
			// 执行任务获取结果
			Object result = future.get(timeout, TimeUnit.MILLISECONDS);
			return result;
		} catch (InterruptedException e) {
			logger.error("SingleThreadQueue任务发生中断异常{}，name：{},异常", name, e);
			return null;
		} catch (ExecutionException e) {
			logger.error("SingleThreadQueue任务发生异常{}，name：{},异常", name, e);
			throw e.getCause();
		} catch (TimeoutException e) {
			logger.error("SingleThreadQueue任务发生超时异常，name：{}", name);
			throw e;
		} finally {
			future.cancel(false);
		}
	}

	/**
	 * 定义拦截器顺序
	 */
	@Override
	public int getOrder() {
		return 2;
	}

}
