package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.UserResponse;
import org.library.payload.ApiResponse;
import org.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{
	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers()
	{
		return ApiResponse.okResponse(userService.getAllUsers());
	}
}
