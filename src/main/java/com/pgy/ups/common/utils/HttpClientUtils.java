package com.pgy.ups.common.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	private static final int RESPONSE_SUCCESS=200;
	
	/**
	 * 发送http请求，返回响应字符串
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getRequest(String url,int timeout) {
		return getRequest(url,null,timeout);
	}
    
	/**
	 * 发送http请求，返回响应字符串
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getRequest(String url, Map<String, Object> params,int timeout) {
		String paramStr = transferToRequestStr(params);
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//设置超时时间为timeout毫秒
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();	
		CloseableHttpResponse response=null;
		try {
			HttpGet postRequest = new HttpGet(new URI(url  + paramStr));
			
			postRequest.setConfig(requestConfig);
			response = httpClient.execute(postRequest);
			//获取应答码
			int responseCode = response.getStatusLine().getStatusCode();
			//返回报文
			String resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			if(responseCode!=RESPONSE_SUCCESS) {
				logger.error("httpClient发送请求失败！请求地址:"+url + ",报文：" + resultStr+",状态码：" + responseCode);
			}else {
				return resultStr;
			}			
			return StringUtils.EMPTY;
		} catch (URISyntaxException | IOException e) {
			logger.error("httpClient发送请求失败{}", ExceptionUtils.getStackTrace(e));			
		}finally {
			try {
				if(response!=null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				logger.error("httpClient关闭失败{}", ExceptionUtils.getStackTrace(e));
			}
		}
		return StringUtils.EMPTY;
	}

	
	
	public static String postRequest(String url,Map<String,String> params,int timeout) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		//设置超时时间为timeout毫秒
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
		httpPost.setConfig(requestConfig);
		//构建报文参数 
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(MapUtils.isNotEmpty(params)) {
			for(Entry<String,String> entry:params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}		
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			response = httpclient.execute(httpPost);
			int responseCode = response.getStatusLine().getStatusCode();
			//响应码必须为200
			if(responseCode!=RESPONSE_SUCCESS) {
				logger.error("HttpClient调用异常：{}", EntityUtils.toString(response.getEntity()));
				return StringUtils.EMPTY;
			}
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			logger.error("HttpClient调用异常：{}", ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if(response!=null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				logger.error("HttpResponse关闭异常：{}", ExceptionUtils.getStackTrace(e));
			}
		}
		return StringUtils.EMPTY;
	}
	
	
	/**
	 * map參數轉string
	 * 
	 * @param paramMap
	 * @return
	 */
	public static String transferToRequestStr(Map<String, Object> paramMap) {
		if (MapUtils.isEmpty(paramMap)) {
			return StringUtils.EMPTY;
		}
		String params = "";
		for (String key : paramMap.keySet()) {
			if (!key.isEmpty()) {
				params += key + "=" + paramMap.get(key) + "&";
			}
		}
		// 去掉最後一個&加上url后的?
		params = "?"+params.substring(0, params.length() - 1);
		return params;
	}

}
