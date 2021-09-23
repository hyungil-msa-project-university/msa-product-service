package com.hyungil.productservice.global.common.error.handler;

import com.hyungil.productservice.global.common.error.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProductExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		log.debug(exception.getBindingResult().getFieldError().getDefaultMessage());
		return ErrorResponseDto.of(exception.getBindingResult().getFieldError().getDefaultMessage());
	}

}