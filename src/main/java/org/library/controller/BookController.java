package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.core.ApiResponse;
import org.library.dto.BookResponse;
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
		return ResponseEntity.ok().body(ApiResponse.<List<BookResponse>>create().data(allBooks).build());
	}
}
