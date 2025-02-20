package com.nerdy.monologue.common.Exception;

import com.nerdy.monologue.common.ResponseCode;
import com.nerdy.monologue.common.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 개발자가 발생시킨 에러
	@ExceptionHandler(com.nerdy.monologue.common.Exception.CustomException.class)
	public ResponseEntity<Response<String>> handleCustomException(com.nerdy.monologue.common.Exception.CustomException e) {
		Response<String> response = Response.fail(e.getResponseCode(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	// 서버 에러
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<String>> handleAllExceptions(Exception e) {
		Response<String> response = Response.fail(ResponseCode.SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
