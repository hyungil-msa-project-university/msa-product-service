package com.hyungil.productservice.global.error.exception;

public class DuplicateRequestException extends RuntimeException{

	public DuplicateRequestException(String message) {
		super(message);
	}
}
