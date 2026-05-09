package org.library.core;

import lombok.Getter;

@Getter
public class ApiResponse<T>
{
	private final boolean success;
	private final T data;

	private ApiResponse(Builder<T> builder)
	{
		this.success = builder.success;
		this.data = builder.data;
	}

	public static <T> Builder<T> create()
	{
		return new Builder<>();
	}

	public static class Builder<T>
	{
		private boolean success = true;
		private T data;

		public Builder<T> success(boolean success)
		{
			this.success = success;
			return this;
		}

		public Builder<T> data(T data)
		{
			this.data = data;
			return this;
		}

		public ApiResponse<T> build()
		{
			return new ApiResponse<>(this);
		}

		public Builder<T> success()
		{
			return this;
		}
	}
}
