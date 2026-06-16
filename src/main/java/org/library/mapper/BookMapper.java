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
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAvailableCopies(request.getAvailableCopies());
		book.setCategory(category);
		book.setAuthors(authors);
		return book;
	}
}