package com.pgy.ups.common.exception;


/**
 * 业务验证异常 
 * 运行时异常
 * @author acer
 *
 */
public class BussinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4097370868661431919L;



    private String code;

	
	public BussinessException(String message) {
		super(message);
	}

    public BussinessException(String code,String message) {
        super(message);
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
