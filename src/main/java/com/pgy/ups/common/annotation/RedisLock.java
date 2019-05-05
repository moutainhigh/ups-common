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
public @interface RedisLock {
	
	//锁的名称(非必填)
	String name() default "redis锁";
	
	//锁的key值
	String key() ;
	
	//默认锁10秒自动失效
	int expireTime() default 10000;
		
}
