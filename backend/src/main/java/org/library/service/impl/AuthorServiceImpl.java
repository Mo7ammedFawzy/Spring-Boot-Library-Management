package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.entity.Author;
import org.library.exception.ResourceNotFoundException;
import org.library.mapper.AuthorMapper;
import org.library.repository.AuthorRepository;
import org.library.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService
{

	private final AuthorRepository authorRepository;

	public List<AuthorResponse> getAllAuthors()
	{
		List<Author> authors = this.authorRepository.findAll();
		return authors.stream().map(AuthorMapper::toResponse).toList();
	}

	@Override
	public AuthorResponse createAuthor(AuthorRequest request)
	{
		Author author = AuthorMapper.toEntity(request);
		Author createdAuthor = this.authorRepository.save(author);
		return AuthorMapper.toResponse(createdAuthor);
	}

	@Override
	public AuthorResponse updateAuthor(Long id, AuthorRequest request)
	{
		Author author = this.authorRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.create(Author.class, id));
		author.setName(request.name());
		Author updatedAuthor = this.authorRepository.save(author);
		return AuthorMapper.toResponse(updatedAuthor);
	}

	@Override
	public void deleteAuthor(Long id)
	{
		Author author = this.authorRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.create(Author.class, id));
		this.authorRepository.delete(author);
	}
}
