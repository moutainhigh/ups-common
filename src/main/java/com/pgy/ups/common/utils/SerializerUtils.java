package com.pgy.ups.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class SerializerUtils {

	private static Logger logger = LoggerFactory.getLogger(SerializerUtils.class);
    
	/**
	 * hession序列化
	 * @param t
	 * @return
	 */
	public static <T> byte[] serialize(T t) {
		byte[] bytes = null;
		// 1、创建字节输出流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		HessianOutput hessianOutput = new HessianOutput(bos);
		try {
			hessianOutput.writeObject(t);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			logger.error("hession序列化对象失败：{}", t);
		}
		return bytes;
	}
	
	/**
	 * hession返序列化
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T deserialize(byte[] bytes) {
		if(Objects.isNull(bytes)) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		HessianInput hessianInput = new HessianInput(bis);
		Object object = null;
		 try {
			object = hessianInput.readObject();
		} catch (IOException e) {
			logger.error("hession返序列化对象失败：{}",ExceptionUtils.getStackTrace(e));
		}
		 return (T)object;
		
	}

}
