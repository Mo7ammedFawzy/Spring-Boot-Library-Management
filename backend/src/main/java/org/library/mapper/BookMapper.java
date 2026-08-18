package org.library.mapper;

import org.library.dto.*;
import org.library.entity.*;

import java.util.List;

public class BookMapper
{

	public static BookResponse toResponse(Book book)
	{
		CategoryResponse categoryResponse = book.getCategory() != null ? CategoryMapper.toResponse(book.getCategory()) : null;
		List<AuthorResponse> authorResponses = book.getAuthors() != null
				? book.getAuthors().stream().map(AuthorMapper::toResponse).toList()
				: List.of();
		return new BookResponse(book.getId(), book.getTitle(), book.getDescription(), book.getAvailableCopies(), categoryResponse, authorResponses);
	}

	public static Book toEntity(BookRequest request, Category category, List<Author> authors)
	{
		return Book.builder().title(request.title()).description(request.description()).availableCopies(request.availableCopies())
				.category(category).authors(authors).build();

	}
}