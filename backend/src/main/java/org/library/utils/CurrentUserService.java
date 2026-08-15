package org.library.utils;

import org.library.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

public class CurrentUserService
{
	public static User getCurrentUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtils.isEmpty(authentication))
			return null;
		return (User) authentication.getPrincipal();
	}

}
