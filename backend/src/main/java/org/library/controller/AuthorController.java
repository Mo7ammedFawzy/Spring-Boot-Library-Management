package org.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.entity.Author;
import org.library.payload.ApiResponse;
import org.library.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController
{
	private final AuthorService authorService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAllAuthors()
	{
		List<AuthorResponse> response = this.authorService.getAllAuthors();
		return ApiResponse.okResponse(response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(@Valid @RequestBody AuthorRequest request)
	{
		AuthorResponse response = this.authorService.createAuthor(request);
		return ApiResponse.createdResponse(response, Author.class);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request)
	{
		AuthorResponse response = this.authorService.updateAuthor(id, request);
		return ApiResponse.okResponse(response, "Author updated successfully!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteAuthor(@PathVariable Long id)
	{
		this.authorService.deleteAuthor(id);
		return ApiResponse.okResponse(null, "Author deleted successfully!");
	}
}
