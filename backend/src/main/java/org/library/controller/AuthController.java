package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.payload.ApiResponse;
import org.library.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request)
	{
		AuthResponse response = authService.register(request);
		return ApiResponse.okResponse(response, "User Registered Successfully!");
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request)
	{
		AuthResponse response = authService.login(request);
		return ApiResponse.okResponse(response);
	}
}
