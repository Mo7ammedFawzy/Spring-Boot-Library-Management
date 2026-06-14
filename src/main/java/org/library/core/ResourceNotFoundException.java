package org.library.core;

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
}