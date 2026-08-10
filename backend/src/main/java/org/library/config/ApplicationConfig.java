package org.library.config;

import lombok.RequiredArgsConstructor;
import org.library.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationConfig
{
	private final UserRepository userRepository;

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService()
	{
		return email -> userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
//		provider.setUserDetailsService(userDetailsService());
//		provider.setPasswordEncoder(passwordEncoder());
//		return provider;
//	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
		return config.getAuthenticationManager();
	}
}
