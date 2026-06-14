package org.library.core;

import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(boolean success, T data, String message)
{
	private static <T> ApiResponse<T> ok(T data)
	{
		return new ApiResponse<>(true, data, null);
	}

	public static <T> ApiResponse<T> error(String message)
	{
		return new ApiResponse<>(false, null, message);
	}

	public static <T> ResponseEntity<ApiResponse<T>> okResponse(T data)
	{
		return ResponseEntity.ok(ApiResponse.ok(data));
	}
}