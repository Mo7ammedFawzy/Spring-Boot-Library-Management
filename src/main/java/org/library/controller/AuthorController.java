package org.library.controller;

import lombok.AllArgsConstructor;
import org.library.core.ApiResponse;
import org.library.dto.*;
import org.library.entity.Author;
import org.library.service.impl.AuthorServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController
{
	private AuthorServiceImpl authorService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAllAuthors()
	{
		List<AuthorResponse> response = this.authorService.getAllAuthors();
		return ApiResponse.okResponse(response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(@RequestBody AuthorRequest request)
	{
		AuthorResponse response = this.authorService.createAuthor(request);
		return ApiResponse.createdResponse(response, Author.class);
	}
}
