package org.library.exception;

public class BorrowNotFoundException extends ResourceNotFoundException
{
	public BorrowNotFoundException()
	{
		super("Borrow record not found");
	}
}