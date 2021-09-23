package com.hyungil.productservice.global.common.error.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {

	public String message;

	public static ErrorResponseDto of(final String message) {
		return new ErrorResponseDto(message);
	}

	public static ErrorResponseDto of(FieldError fieldError) {
		if (fieldError == null) {
			return new ErrorResponseDto("invalid params");
		} else {
			return new ErrorResponseDto(fieldError.getDefaultMessage());
		}
	}
}
