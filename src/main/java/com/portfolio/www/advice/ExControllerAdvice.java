package com.portfolio.www.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorResult>> methodArgNotValidHandle(MethodArgumentNotValidException e) {
		List<ErrorResult> errors = new ArrayList<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.add(new ErrorResult(field, message));
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<List<ErrorResult>> handleMaxSizeException(MaxUploadSizeExceededException e) {
		List<ErrorResult> errors = new ArrayList<>();
		errors.add(new ErrorResult("attFile", "Maximum upload size of 10485760 bytes exceeded"));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
	
	@Data
	@AllArgsConstructor
	public static class ErrorResult {
		private String field;
		private String message;
	}
	
}
