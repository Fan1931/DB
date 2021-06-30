package com.cy.pj.common.exception;

public class ServiceException extends RuntimeException{
	private static final long serialVersionUID = -3135239055465723987L;
	public ServiceException() {
		super();
	}
	public ServiceException(String message) {
		super(message);
	}
	public ServiceException(Throwable cause) {
		super(cause);
	}
}
