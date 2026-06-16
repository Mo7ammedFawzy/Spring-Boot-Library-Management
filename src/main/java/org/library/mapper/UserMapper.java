package org.library.mapper;

import org.library.dto.UserResponse;
import org.library.entity.User;

public class UserMapper
{
	public static UserResponse toResponse(User user)
	{
		return new UserResponse(user.getId(),user.getName(),user.getEmail());
	}
}
