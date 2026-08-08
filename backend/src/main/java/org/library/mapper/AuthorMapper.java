package org.library.mapper;

import org.library.dto.*;
import org.library.entity.Author;

public class AuthorMapper
{

	public static AuthorResponse toResponse(Author author)
	{
		return new AuthorResponse(author.getId(), author.getName());
	}

	public static Author toEntity(AuthorRequest request)
	{
		Author author = new Author();
		author.setName(request.getName());
		return author;
	}
}
