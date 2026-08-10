package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.payload.ApiResponse;
import org.library.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
	private final AuthServiceImpl authServiceImpl;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request){
		AuthResponse response = authServiceImpl.register(request);
		return ApiResponse.okResponse(response,"User Registered Successfully!");
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request){
		AuthResponse response = authServiceImpl.login(request);
		return ApiResponse.okResponse(response);
	}
}
