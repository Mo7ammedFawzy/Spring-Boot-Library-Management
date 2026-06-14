package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.core.ResourceNotFoundException;
import org.library.dto.*;
import org.library.entity.*;
import org.library.mapper.BookMapper;
import org.library.repository.*;
import org.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService
{
	private final BookRepository bookRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public BookResponse createBook(BookRequest request)
	{
		Category category = this.categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(Category.class, request.getCategoryId()));
		Book entity = BookMapper.toEntity(request,category);
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
		Category category = this.categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(Category.class, request.getCategoryId()));
		Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Book.class,id));
		book.setTitle(request.getTitle());
		book.setDescription(request.getDescription());
		book.setAvailableCopies(request.getAvailableCopies());
		book.setCategory(category);
		return BookMapper.toResponse(book);
	}

	@Override
	public void deleteBook(Long id)
	{
		Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Book.class, id));
		bookRepository.delete(book);
	}

	@Override
	public void deleteBooks()
	{
		long count = this.bookRepository.count();
		if (count == 0)
			throw new ResourceNotFoundException(Book.class);
		bookRepository.deleteAll();
	}
}