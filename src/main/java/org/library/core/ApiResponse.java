package org.library.core;

import org.springframework.http.ResponseEntity;

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
}