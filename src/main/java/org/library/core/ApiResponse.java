package org.library.core;

import org.springframework.http.*;

public record ApiResponse<T>(boolean success, T data, String message)
{
	public static <T> ApiResponse<T> ok(T data)
	{
		return new ApiResponse<>(true, data, null);
	}

	private static <T> ApiResponse<T> ok(T data, String message)
	{
		return new ApiResponse<>(true, data, message);
	}

	public static <T> ApiResponse<T> error(String message)
	{
		return new ApiResponse<>(false, null, message);
	}

	public static <T> ResponseEntity<ApiResponse<T>> errorResponse(String message)
	{
		return ResponseEntity.badRequest().body(ApiResponse.error(message));
	}

	public static <T> ResponseEntity<ApiResponse<T>> okResponse(T data)
	{
		return ResponseEntity.ok(ApiResponse.ok(data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> okResponse(T data, String message)
	{
		return ResponseEntity.ok(ApiResponse.ok(data, message));
	}

	public static <T> ResponseEntity<ApiResponse<T>> createdResponse(T response)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
	}

	public static <T> ResponseEntity<ApiResponse<T>> createdResponse(T response, Class<?> className)
	{
		String message = className.getSimpleName() + " created successfully!";
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, message));
	}
}