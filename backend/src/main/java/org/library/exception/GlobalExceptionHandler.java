package org.library.exception;

import org.library.payload.ApiError;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFoundException(ResourceNotFoundException exception)
	{
		return ApiError.notFound(exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex)
	{
		return ApiError.validationError(ex);
	}

	@ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
	public ResponseEntity<ApiError> handleBadCredentials(Exception exception)
	{
		return ApiError.unauthorized(exception.getMessage());
	}

	@ExceptionHandler(BookUnavailableException.class)
	public ResponseEntity<ApiError> handleUnAvailableBook(BookUnavailableException exception)
	{
		return ApiError.ofResponse(HttpStatus.CONFLICT, exception.getMessage());
	}
}