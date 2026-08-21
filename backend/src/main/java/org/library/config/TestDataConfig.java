package org.library.config;

import lombok.RequiredArgsConstructor;
import org.library.entity.User;
import org.library.enums.Role;
import org.library.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class TestDataConfig
{
	private static final String TEST_EMAIL = "test@example.com";
	private static final String TEST_PASSWORD = "test123456";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	public ApplicationRunner testUserSeeder()
	{
		return args -> userRepository.findByEmail(TEST_EMAIL)
				.orElseGet(() -> userRepository.save(User.builder()
						.name("Test User")
						.role(Role.ADMIN)
						.email(TEST_EMAIL)
						.password(passwordEncoder.encode(TEST_PASSWORD))
						.build()));
	}
}
