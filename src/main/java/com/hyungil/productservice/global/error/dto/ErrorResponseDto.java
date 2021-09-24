package com.hyungil.productservice.global.error.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {

	public String message;
	private static final String INVALID_PARAMS = "invalid params";

	public static ErrorResponseDto of(final String message) {
		return new ErrorResponseDto(message);
	}

	public static ErrorResponseDto of(FieldError fieldError) {
		if (fieldError == null) {
			return new ErrorResponseDto(INVALID_PARAMS);
		} else {
			return new ErrorResponseDto(fieldError.getDefaultMessage());
		}
	}
}
