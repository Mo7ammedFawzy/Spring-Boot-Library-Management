package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.core.ApiResponse;
import org.library.dto.*;
import org.library.service.impl.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController
{
	private final BookServiceImpl bookService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks()
	{
		List<BookResponse> allBooks = this.bookService.getAllBooks();
		return ApiResponse.okResponse(allBooks);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody BookRequest bookRequest)
	{
		BookResponse createdBook = this.bookService.createBook(bookRequest);
		return ApiResponse.okResponse(createdBook,"Book created successfully!");
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Long id)
	{
		BookResponse book = this.bookService.getBookById(id);
		return ApiResponse.okResponse(book);
	}
	@GetMapping("/categories/{categoryId}")
	public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksByCategoryId(@PathVariable Long categoryId)
	{
		List<BookResponse> books = this.bookService.getByCategoryId(categoryId);
		return ApiResponse.okResponse(books);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable Long id, @RequestBody BookRequest bookRequest)
	{
		BookResponse bookResponse = this.bookService.updateBook(id, bookRequest);
		return ApiResponse.okResponse(bookResponse,"Book updated successfully!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id)
	{
		this.bookService.deleteBook(id);
		return ApiResponse.okResponse(null, "Book deleted successfully!");
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<String>> deleteBooks(){
		this.bookService.deleteBooks();
		return ApiResponse.okResponse(null, "Books deleted successfully!");
	}
}
