package org.library.exception;

public class BookAlreadyReturnedException extends RuntimeException
{
	public BookAlreadyReturnedException()
	{
		super("Book Already Returned");
	}
}