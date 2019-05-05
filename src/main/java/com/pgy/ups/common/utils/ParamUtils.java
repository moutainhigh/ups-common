package com.pgy.ups.common.utils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.pgy.ups.common.exception.ParamValidException;




public class ParamUtils {


	// 初始查询页数
	private static final Integer INIT_PAGE_NUM = 1;

	// 默认的每页查询数量
	private static final Integer DEFAULT_PAGE_SIZE = 10;

	/**
	 * 取出HttpServletRequest中的所有参数
	 * 
	 * @param request
	 * @return map
	 */
	private ParamUtils() {

	}


	public static Map<String, Object> getParamterMap(HttpServletRequest request) {
		Map<String, Object> params = new LinkedHashMap<>();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			Object value = request.getParameter(key);
			params.put(key, value);
		}
		return params;
	}

	/**
	 * 取出HttpServletRequest中的所有参数并封装 分页参数
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getPageParamterMap(HttpServletRequest request) {
		Map<String, Object> params = new LinkedHashMap<>();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			Object value = request.getParameter(key);
			params.put(key, value);
		}
		Set<String> set = params.keySet();
		if (!set.contains("pageNumber")) {
			params.put("pageNumber", INIT_PAGE_NUM);
		}
		if (!set.contains("pageSize")) {
			params.put("pageSize", DEFAULT_PAGE_SIZE);
		}
		return params;
	}

	/**
	 * 参数不能有null
	 * 
	 * @param params
	 * @return
	 * @throws ParamValidException 
	 */
	@SuppressWarnings("rawtypes")
	public static void assertNotNull(Object... params) throws ParamValidException {
		for (int i = 0; i < params.length; i++) {
			if (Objects.isNull(params[i])) {
				throw new ParamValidException("第" + i + "个参数为null");		
			}
			if (params[i] instanceof String && (StringUtils.isEmpty(params[i].toString()))) {
				throw new ParamValidException("第" + i + "个参数为空字符串");		
			}
			if (params[i] instanceof Collection && ((Collection) params[i]).isEmpty()) {
				throw new ParamValidException("第" + i + "个参数为空集合");		
			}
			if (params[i] instanceof Map && ((Map) params[i]).isEmpty()) {
				throw new ParamValidException("第" + i + "个参数为空map");		
			}
		}
	}

    /**
          * 指定正则验证
     * @param service
     * @param string
     * @return 
     * @throws ParamValidException 
     */
	public static  void validateByExp(String str, String regex) throws ParamValidException {
		Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(str);
        if(m.matches())	{
        	return;
        }
        throw new ParamValidException("参数不合法！参数："+str);
	}
}
