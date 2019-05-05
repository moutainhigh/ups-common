package com.pgy.ups.common.exception;

public class RedisLockFailException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4924836621150756440L;
	
	
    public RedisLockFailException() {
    	super("redis锁获取失败异常！");
    }


}
