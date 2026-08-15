package org.library.exception;

public class BookUnavailableException extends RuntimeException
{
	public BookUnavailableException()
	{
		super("Book is not Available");
	}
}
