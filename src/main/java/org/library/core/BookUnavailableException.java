package org.library.core;

public class BookUnavailableException extends RuntimeException
{
	public BookUnavailableException(String message)
	{
		super(message);
	}
}
