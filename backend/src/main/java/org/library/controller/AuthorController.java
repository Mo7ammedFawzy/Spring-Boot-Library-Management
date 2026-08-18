package org.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.entity.Author;
import org.library.exception.ResourceNotFoundException;
import org.library.mapper.AuthorMapper;
import org.library.payload.ApiResponse;
import org.library.repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController
{
	private final AuthorRepository authorRepository;

	@GetMapping
	public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAllAuthors()
	{
		List<AuthorResponse> response = this.authorRepository.findAll()
				.stream()
				.map(AuthorMapper::toResponse)
				.toList();
		return ApiResponse.okResponse(response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(@Valid @RequestBody AuthorRequest request)
	{
		Author author = AuthorMapper.toEntity(request);
		Author createdAuthor = this.authorRepository.save(author);
		AuthorResponse response = AuthorMapper.toResponse(createdAuthor);
		return ApiResponse.createdResponse(response, Author.class);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request)
	{
		Author author = this.authorRepository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.create(Author.class, id));
		author.setName(request.name());
		Author updatedAuthor = this.authorRepository.save(author);
		AuthorResponse response = AuthorMapper.toResponse(updatedAuthor);
		return ApiResponse.okResponse(response, "Author updated successfully!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteAuthor(@PathVariable Long id)
	{
		Author author = this.authorRepository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.create(Author.class, id));
		this.authorRepository.delete(author);
		return ApiResponse.okResponse(null, "Author deleted successfully!");
	}
}
