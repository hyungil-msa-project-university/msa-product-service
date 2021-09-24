package com.hyungil.productservice.global.error.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {

	private String message;

	public static ErrorResponseDto of(final String message) {
		return new ErrorResponseDto(message);
	}

	public static ErrorResponseDto of(final FieldError fieldError) {
		return new ErrorResponseDto(fieldError.getDefaultMessage());
	}

}
