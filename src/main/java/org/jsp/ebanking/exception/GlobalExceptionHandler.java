package org.jsp.ebanking.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.jsp.ebanking.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handle(MethodArgumentNotValidException exception) {
		List<String> errorMessages = exception.getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		return ResponseEntity.status(400).body(new ErrorDto(errorMessages));
	}

	@ExceptionHandler(DataExistsException.class)
	public ResponseEntity<Object> handle(DataExistsException exception) {
		return ResponseEntity.status(409).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(FailedToSendOtpException.class)
	public ResponseEntity<Object> handle(FailedToSendOtpException exception) {
		return ResponseEntity.status(500).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(ExpiredException.class)
	public ResponseEntity<Object> handle(ExpiredException exception) {
		return ResponseEntity.status(408).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(MissMatchException.class)
	public ResponseEntity<Object> handle(MissMatchException exception) {
		return ResponseEntity.status(400).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handle(DataNotFoundException exception) {
		return ResponseEntity.status(404).body(new ErrorDto(exception.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ErrorDto handle(BadCredentialsException exception) {
		return new ErrorDto("Invalid Password");
	}
	
	@ExceptionHandler(PaymentFailedException.class)
	public ResponseEntity<Object> handle(PaymentFailedException exception) {
		return ResponseEntity.status(502).body(new ErrorDto(exception.getMessage()));
	}

}