package org.library.service;

import org.library.dto.*;

import java.util.List;

public interface BookService
{
	BookResponse createBook(BookRequest request);

	List<BookResponse> getAllBooks();

	BookResponse getBookById(Long id);

	BookResponse updateBook(Long id, BookRequest request);

	void deleteBook(Long id);

	void deleteBooks();

	List<BookResponse> getByCategoryId(Long categoryId);
}