package com.bnguimgo.springbootrestserver.exception;

public class TechnicalErrorException extends RuntimeException {

	private static final long serialVersionUID = -811807278404114373L;
	
	private Long id;

	public TechnicalErrorException() {
        super();
    }

	public TechnicalErrorException(String message) {
        super(message);
    }

	public TechnicalErrorException(Throwable cause) {
        super(cause);
    }
	
    public TechnicalErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public TechnicalErrorException(Long id) {
    	super(id.toString());
        this.id = id;
    }
     
    public Long getId() {
        return id;
    }
    public void setId(Long id){
    	this.id = id;
    }
}