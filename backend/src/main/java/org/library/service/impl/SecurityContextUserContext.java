package org.library.service.impl;

import org.library.entity.User;
import org.library.service.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class SecurityContextUserContext implements UserContext
{
	@Override
	public User getCurrentUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtils.isEmpty(authentication))
			return null;
		return (User) authentication.getPrincipal();
	}
}