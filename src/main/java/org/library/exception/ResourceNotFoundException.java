package org.library.exception;

public class ResourceNotFoundException extends RuntimeException
{
	public ResourceNotFoundException(Class<?> resource, Object id)
	{
		super(resource.getSimpleName() + " not found with id: " + id);
	}

	public ResourceNotFoundException(Class<?> resource)
	{
		super(resource.getSimpleName() + "s" + " not found");
	}

	public ResourceNotFoundException(String message)
	{
		super(message);
	}
}