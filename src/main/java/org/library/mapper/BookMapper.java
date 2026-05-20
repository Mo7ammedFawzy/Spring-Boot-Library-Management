package org.library.mapper;

import org.library.dto.*;
import org.library.entity.Book;

public class BookMapper
{

	public static BookResponse toResponse(Book book)
	{
		return new BookResponse(book.getId(), book.getTitle(), book.getDescription(), book.getAvailableCopies());
	}

	public static Book toEntity(BookRequest request)
	{
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAvailableCopies(request.getAvailableCopies());
		return book;
	}
}