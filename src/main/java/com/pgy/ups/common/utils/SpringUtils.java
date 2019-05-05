package com.pgy.ups.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * spring环境
 * 
 * @author 墨凉
 *
 */

@Component
public class SpringUtils implements ApplicationContextAware{

	private static ApplicationContext applicationContext; // Spring应用上下文环境

	/*
	 * 实现了ApplicationContextAware 接口，必须实现该方法；
	 * 通过传递applicationContext参数初始化成员变量applicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String className) {
		return (T) applicationContext.getBean(className);
	}
    
	/**
	 * 获取指定类型的实体类集合
	 * @param clazz
	 * @return
	 */
	public static<T> List<T> getBeans(Class<T> clazz) {
		Map<String, T> map=applicationContext.getBeansOfType(clazz);
		if(!CollectionUtils.isEmpty(map)) {
			List<T> list=new ArrayList<>();
			for(Entry<String,T> e:map.entrySet()) {				
				list.add(e.getValue());
			}
			return list;
		}
		return Collections.emptyList();		
	}
	
	/**
	 * 获取指定类型的实体类集合
	 * @param clazz
	 * @return
	 */
	public static<T> Map<String, T>  getBeansMap(Class<T> clazz) {
		return applicationContext.getBeansOfType(clazz);		
	}
	

}