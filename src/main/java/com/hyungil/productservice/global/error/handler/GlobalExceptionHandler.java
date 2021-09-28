package com.hyungil.productservice.global.error.handler;

import com.hyungil.productservice.global.error.dto.ErrorResponseDto;
import com.hyungil.productservice.global.error.exception.DuplicateRequestException;
import com.hyungil.productservice.global.error.exception.NotFoundRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		String message = exception.getBindingResult().getFieldError().getDefaultMessage();
		log.debug(message);

		return ErrorResponseDto.of(message);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundRequestException.class)
	public ErrorResponseDto handleNotFoundException(NotFoundRequestException exception) {

		String message = exception.getMessage();
		log.debug(message, exception);

		return ErrorResponseDto.of(message);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DuplicateRequestException.class)
	public ErrorResponseDto handleDuplicateRequestException(DuplicateRequestException exception) {

		String message = exception.getMessage();
		log.debug(message, exception);

		return ErrorResponseDto.of(message);
	}
}