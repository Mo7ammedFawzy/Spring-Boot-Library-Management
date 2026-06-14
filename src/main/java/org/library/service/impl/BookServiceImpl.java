package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.core.ResourceNotFoundException;
import org.library.dto.*;
import org.library.entity.Book;
import org.library.mapper.BookMapper;
import org.library.repository.BookRepository;
import org.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService
{
	private final BookRepository bookRepository;

	@Override
	public BookResponse createBook(BookRequest request)
	{
		Book entity = BookMapper.toEntity(request);
		Book createdBook = this.bookRepository.save(entity);
		return BookMapper.toResponse(createdBook);
	}

	@Override
	public List<BookResponse> getAllBooks()
	{
		List<Book> all = bookRepository.findAll();
		return all.stream().map(BookMapper::toResponse).toList();
	}

	@Override
	public BookResponse getBookById(Long id)
	{
		Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Book.class,id));
		return BookMapper.toResponse(book);
	}

	@Override
	public BookResponse updateBook(Long id, BookRequest request)
	{

		Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Book.class,id));
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAvailableCopies(request.getAvailableCopies());
		return BookMapper.toResponse(book);
	}

	@Override
	public void deleteBook(Long id)
	{
		bookRepository.deleteById(id);
	}
}