package org.library.service.impl;

import org.library.entity.User;
import org.library.enums.Role;
import org.library.service.UserContext;

public class FakeUserContext implements UserContext
{
	private final User fakeUser;

	public FakeUserContext()
	{
		this.fakeUser = User.builder()
				.id(1L)
				.name("Test User")
				.email("test@example.com")
				.role(Role.USER)
				.build();
	}

	public FakeUserContext(User user)
	{
		this.fakeUser = user;
	}

	@Override
	public User getCurrentUser()
	{
		return fakeUser;
	}
}