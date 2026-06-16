package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.dto.UserResponse;
import org.library.entity.User;
import org.library.mapper.UserMapper;
import org.library.repository.UserRepository;
import org.library.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
	private UserRepository userRepository;

	@Override
	public List<UserResponse> getAllUsers()
	{
		List<User> users = this.userRepository.findAll();
		return users.stream().map(UserMapper::toResponse).toList();
	}
}
