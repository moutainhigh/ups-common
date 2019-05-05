package com.pgy.ups.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @author 墨凉
 *
 */
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface SingleThreadQueue {
	
	//任务的名称(非必填)
	String name() default "default task";
	
	//默认60秒超时
	int timeout() default 60000;
		
}
