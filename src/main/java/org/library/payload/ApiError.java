package org.library.payload;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public record ApiError(int status, String message, LocalDateTime timestamp)
{

	public static ResponseEntity<ApiError> notFound(String message)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(of(HttpStatus.NOT_FOUND, message));
	}

	private static ApiError of(HttpStatus status, String message)
	{
		return new ApiError(status.value(), message, LocalDateTime.now());
	}

	public static ResponseEntity<ApiError> validationError(MethodArgumentNotValidException ex)
	{
		String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage())
				.collect(Collectors.joining(", "));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(of(HttpStatus.BAD_REQUEST, message));
	}
}
