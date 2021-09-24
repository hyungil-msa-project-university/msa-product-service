package com.hyungil.productservice.global.error.exception;

public class InvalidRequestException extends RuntimeException {

	public InvalidRequestException(final String message) {
		super(message);
	}

}
