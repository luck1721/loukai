package com.example.demo.bll.exception;

import lombok.Getter;

/**
 * @author lk
 * @date 2021/3/4
 */
@Getter
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;

	public ApplicationException() {
		this.errorCode = -1;
	}

	public ApplicationException(String errorMessage) {
		super(errorMessage);
		this.errorCode = -1;
		this.errorMessage = errorMessage;
	}

	public ApplicationException(int errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ApplicationException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = -1;
		this.errorMessage = errorMessage;
	}

	public ApplicationException(int errorCode, String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
		this.errorCode = -1;
	}

}
