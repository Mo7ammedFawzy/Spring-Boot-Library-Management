package org.library.mapper;

import org.library.dto.*;
import org.library.entity.*;

public class BookMapper
{

	public static BookResponse toResponse(Book book)
	{
		return new BookResponse(book.getId(), book.getTitle(), book.getDescription(), book.getAvailableCopies(), book.getCategory());
	}

	public static Book toEntity(BookRequest request, Category category)
	{
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAvailableCopies(request.getAvailableCopies());
		book.setCategory(category);
		return book;
	}
}