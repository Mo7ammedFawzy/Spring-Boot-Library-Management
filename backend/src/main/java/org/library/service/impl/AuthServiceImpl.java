package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.entity.User;
import org.library.enums.Role;
import org.library.repository.UserRepository;
import org.library.security.JwtUtil;
import org.library.service.AuthService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;

	@Override
	public AuthResponse register(RegisterRequest request)
	{
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = User.builder().name(request.getName()).role(Role.USER).email(request.getEmail()).password(encodedPassword).build();
		userRepository.save(user);
		String token = jwtUtil.generateToken(user);
		return new AuthResponse(token);
	}

	@Override
	public AuthResponse login(LoginRequest request)
	{
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email is not found"));
		String token = jwtUtil.generateToken(user);
		return new AuthResponse(token);
	}
}
