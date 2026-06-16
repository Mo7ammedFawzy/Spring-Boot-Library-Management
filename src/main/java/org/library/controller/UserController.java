package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.core.ApiResponse;
import org.library.dto.UserResponse;
import org.library.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{
	private UserServiceImpl userService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUser()
	{
		List<UserResponse> users = this.userService.getAllUsers();
		return ApiResponse.okResponse(users);
	}
}
