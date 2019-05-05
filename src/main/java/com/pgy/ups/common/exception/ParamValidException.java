package com.pgy.ups.common.exception;

/**
 * 参数验证异常 
 * 非运行是异常
 * @author acer
 *
 */
public class ParamValidException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2150847971593587409L;
    
	public ParamValidException(String message) {
		super(message);
	}
	

}
