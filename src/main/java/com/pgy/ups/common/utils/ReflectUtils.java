package com.pgy.ups.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pgy.ups.common.exception.ParamValidException;



public class ReflectUtils {
	
	private  static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	
	/**
	 * 通过属性名获取get方法
	 * 
	 * @param fieldName
	 * @param clazz
	 * @return
	 * @throws ParamValidException
	 */
	public static Method acquireGetterMethod(String fieldName, Class<?> clazz) {

		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
			try {
				Method method = clazz.getMethod(getter);
				return method;
			} catch (Exception e) {
				// 不做任何处理 否则会跳出循环
			}
		}
		return null;
	}
	
	/**
	  * 获取所有属性以及父类的属性
	 * @param o
	 * @return
	 */
	public static List<Field> getAllFields(Object o){
	    Class<?> c= o.getClass();
	    List<Field> fieldList = new ArrayList<>();
	    while (c!= null){
	        fieldList.addAll(new ArrayList<>(Arrays.asList(c.getDeclaredFields())));
	        c= c.getSuperclass();
	    }
	    return fieldList;
	}
	
	
	/**
	 * 反射调用 方法
	 * @param getMethod
	 * @param object
	 * @return
	 */
	public static String invokeMethod(Method getMethod, Object object) {

		try {
			Object value = getMethod.invoke(object);
			if (Objects.nonNull(value)) {
				return value.toString();
			}
			return "";
		} catch (Exception e) {
			logger.error("反射调用方法异常：{}", ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}
	
	/**
	 *   通过get方法取出对象值，并拼装成url参数
	 * @param model
	 * @return
	 */
	public static String objectToUrlParams(Object object) {
		//获取所有的属性名称
		List<String> fs=getAllFields(object).stream().map((e)->{return e.getName(); }).collect(Collectors.toList());
		return objectToUrlParams(object,fs.toArray(new String[fs.size()]));
	}
	
	
	/**
	 * 把model对象转为 key=value&key=value字符串形式
	 * 
	 * @param model
	 * @param properties 指定的properties
	 * @return
	 */
	public static String objectToUrlParams(Object object, String[] properties) {
		if (ArrayUtils.isEmpty(properties)) {
			throw new RuntimeException("参数验签规则顺序不能为空！");
		}
		// 拼接url参数字符串 "key=value&key=value&key=value"
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < properties.length; i++) {
			// 通过属性名获取get方法
			Method getterMethod = acquireGetterMethod(properties[i], object.getClass());
			if (Objects.nonNull(getterMethod)) {
				String value = invokeMethod(getterMethod, object);
				sb.append(properties[i]).append("=").append(value);
				// 最后一个参数无需&符号
				if (i != (properties.length - 1)) {
					sb.append("&");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * model对象转map
	 * @param m
	 * @return
	 */
	public static Map<String, String> objectToMap(Object object){
		List<String> fs=getAllFields(object).stream().map((e)->{return e.getName(); }).collect(Collectors.toList());
		Map<String,String> map=new HashMap<>();
		for(String fieldName:fs) {
			Method getterMethod=acquireGetterMethod(fieldName, object.getClass());
			if(Objects.nonNull(getterMethod)) {
				String value=invokeMethod(getterMethod, object);
				map.put(fieldName, value);
			}			
		}
		return map;
	}

}
