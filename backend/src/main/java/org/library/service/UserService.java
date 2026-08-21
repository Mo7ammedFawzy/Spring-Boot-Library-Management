package org.library.service;

import org.library.dto.UserResponse;

import java.util.List;

public interface UserService
{
	List<UserResponse> getAllUsers();
}
