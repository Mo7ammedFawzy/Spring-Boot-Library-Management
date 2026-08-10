package org.library.controller;

import jakarta.validation.Valid;
import lombok.*;
import org.library.entity.*;
import org.library.enums.Role;
import org.library.payload.ApiResponse;
import org.library.dto.*;
import org.library.service.impl.AuthorServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController
{
	private final AuthorServiceImpl authorService;

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
}
