package org.library.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter
{
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService)
	{
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		String authHeader = request.getHeader("Authorization");
		String bearer = "Bearer ";
		if (authHeader == null || !authHeader.startsWith(bearer))
		{
			filterChain.doFilter(request, response);
			return;
		}
		String token = authHeader.substring(bearer.length());
		try
		{
			String email = jwtUtil.extractUserName(token);
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				if (jwtUtil.isTokenValid(token, userDetails))
				{
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		catch (JwtException | IllegalArgumentException e)
		{
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);
	}
}
