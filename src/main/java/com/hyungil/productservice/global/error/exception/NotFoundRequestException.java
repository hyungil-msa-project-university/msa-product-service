package com.hyungil.productservice.global.error.exception;

public class NotFoundRequestException extends RuntimeException {

	public NotFoundRequestException(final String message) {
		super(message);
	}

}
