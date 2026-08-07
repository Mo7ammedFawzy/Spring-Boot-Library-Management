package org.library.service;

import org.library.dto.*;

public interface AuthService
{
	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}
