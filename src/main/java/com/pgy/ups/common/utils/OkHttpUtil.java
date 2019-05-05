package com.pgy.ups.common.utils;

import com.squareup.okhttp.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttpUtil {

    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    static {
        client.setConnectTimeout(21, TimeUnit.SECONDS);
        client.setReadTimeout(21, TimeUnit.SECONDS);
    }

    /**
     * http  get 方法
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException(response.body().string());
        }
    }

    public static String getHostParseRetry(String url) throws IOException {
    	int count = 1;
        Request request = new Request.Builder().url(url).build();
        String result = null;
        do {
			Response response = client.newCall(request).execute();
			result = response.body().string();
			if(response.isSuccessful()){
				break;
			}else if (result.contains("unknown")) {
				count++;
			}else{
				throw new IOException(result);
			}
		} while (count <= 3);
        return result;
    }


    /**
     * http  get获取字节 方法
     *
     * @param url
     * @return byte
     * @throws IOException
     */
    public static byte[] getbyte(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().bytes();
        } else {
            throw new IOException(response.body().string());
        }
    }


    /**
     * http  get 方法
     *
     * @param url
     * @param token
     * @return
     * @throws IOException
     */
    public static String getWithCookie(String url, String token) throws IOException {
        String tokenStr = "token=" + token;
        Request request = new Request.Builder().header("Cookie", tokenStr).url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException(response.body().string());
        }
    }

    /**
     * http post方法   application/json
     *
     * @param url
     * @param json json字符串参数
     * @return
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(APPLICATION_JSON, json == null ? "" : json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * http post方法   application/json
     *
     * @param url
     * @param json json字符串参数
     * @return
     * @throws IOException
     */
    public static String postSetHeader(String url, String json,String appid) throws IOException {
        RequestBody requestBody = RequestBody.create(APPLICATION_JSON, json == null ? "" : json);
        Request request = new Request.Builder().header("appid",appid).url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     * http post方法   application/json
     *
     * @param url
     * @param json json字符串参数
     * @return
     * @throws IOException
     */
    public static String postWithCookies(String url, String json, String token) throws IOException {
        String tokenStr = "token=" + token;
        RequestBody requestBody = RequestBody.create(APPLICATION_JSON, json == null ? "" : json);
        Request request = new Request.Builder().header("Cookie", tokenStr).url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    public static Response postResponse(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(APPLICATION_JSON, json == null ? "" : json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 表单post提交   application/x-www-form-urlencoded
     *
     * @param url
     * @param paramsMap
     * @return
     * @throws IOException
     */
    public static String postForm(String url, final Map<String,? extends Object> paramsMap) throws IOException {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                formEncodingBuilder.add(key, String.valueOf(paramsMap.get(key)));
            }
        }
        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException(response.body().string());
        }
    }

    /**
     * @author jyq
     * @description postform提交需要重试机制
     * @date 创建时间：2017年4月11日  下午5:16:04
     * @version 1.0.0.0
     * @param url
     * @param paramsMap
     * @return
     * @throws IOException
     */
	public static String postFormHostParseRetry(String url, final Map<String, Object> paramsMap) throws IOException {
		int count = 1;
		FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
		if (paramsMap != null) {
			for (String key : paramsMap.keySet()) {
				formEncodingBuilder.add(key, String.valueOf(paramsMap.get(key)));
			}
		}
		Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();
		String result = null;
		do {
			Response response = client.newCall(request).execute();
			result = response.body().string();
			if(response.isSuccessful()){
				break;
			}else if (result.contains("unknown")) {
				count++;
			}else{
				throw new IOException(result);
			}
		} while (count <= 3);
		return result;
	}


    /**
     * 表单post提交   application/x-www-form-urlencoded
     *
     * @param url
     * @param paramsMap
     * @return
     * @throws IOException
     */
    public static Response postFormWithResponse(String url, final Map<String, Object> paramsMap) throws IOException {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                formEncodingBuilder.add(key, String.valueOf(paramsMap.get(key)));
            }
        }
        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response;
        } else {
            throw new IOException(response.body().string());
        }
    }

    /**
     * 表单post提交   multipart/form-data
     *
     * @param url
     * @param paramsMap
     * @return
     * @throws IOException
     * @author jyq
     * @date 2016年7月19日 下午5:28:28
     */
    public static String postMultiPartForm(String url, final Map<String, Object> paramsMap) throws IOException {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                Object value = paramsMap.get(key);
                if (value instanceof byte[]) {
                    byte[] bytes = (byte[]) value;
                    builder.addFormDataPart(key, key, RequestBody.create(MultipartBuilder.FORM, bytes));
                } else if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, key, RequestBody.create(MultipartBuilder.FORM, file));
                } else {
                    builder.addFormDataPart(key, String.valueOf(value));
                }
            }
        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else if (response.code() == 413) {
           // throw new BusinessException(ErrorCode.ERROR_OTHER_MSG.customDescription("请求流量超过限制大小,请压缩处理后重试"), "请求流量超过限制大小,请压缩处理后重试");
        } else {
            throw new IOException(response.body().string());
        }
        return null;
    }

    /**
     * @author jyq
     * @description 表单post提交（可以重试）
     * @date 创建时间：2017年4月11日  下午5:23:28
     * @version 1.0.0.0
     * @param url
     * @return
     * @throws IOException
     */
    public static String postFileHostParseRetry(String url, final Map<String, Object> paramsMap) throws IOException {
        int count = 1;
    	MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (paramsMap != null) {
            for (String key : paramsMap.keySet()) {
                Object value = paramsMap.get(key);
                if (value instanceof byte[]) {
                    byte[] bytes = (byte[]) value;
                    builder.addFormDataPart(key, key, RequestBody.create(MultipartBuilder.FORM, bytes));
                } else if (value instanceof File) {
                    File file = (File) value;
                    builder.addFormDataPart(key, key, RequestBody.create(MultipartBuilder.FORM, file));
                } else {
                    builder.addFormDataPart(key, String.valueOf(value));
                }
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        String result=null;
        do{
        	 Response response = client.newCall(request).execute();
        	 result = response.body().string();
             if(response.isSuccessful()){
            	 break;
             }else if (response.code() == 413) {
                // throw new BusinessException(ErrorCode.ERROR_OTHER_MSG.customDescription("请求流量超过限制大小,请压缩处理后重试"), "请求流量超过限制大小,请压缩处理后重试");
             } else {
	            if(result.contains("unknown")){
	            	count++;
	            }else{
	            	throw new IOException(result);
	            }
             }
        }while(count<=3);

        return result;

    }
}
