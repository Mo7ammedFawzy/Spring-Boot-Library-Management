package org.library.mapper;

import org.library.dto.*;
import org.library.entity.*;

import java.util.List;

public class BookMapper
{

	public static BookResponse toResponse(Book book)
	{
		return new BookResponse(book.getId(), book.getTitle(), book.getDescription(), book.getAvailableCopies(), book.getCategory(),
				book.getAuthors());
	}

	public static Book toEntity(BookRequest request, Category category, List<Author> authors)
	{
		return Book.builder().title(request.title()).description(request.description()).availableCopies(request.availableCopies())
				.category(category).authors(authors).build();

	}
}