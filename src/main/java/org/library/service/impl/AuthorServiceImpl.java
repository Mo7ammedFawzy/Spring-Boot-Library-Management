package org.library.service.impl;

import lombok.*;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.library.dto.*;
import org.library.entity.Author;
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
		List<Author> authors =  this.authorRepository.findAll();
		return authors.stream().map(AuthorMapper::toResponse).toList();
	}

	@Override
	public AuthorResponse createAuthor(AuthorRequest request)
	{
		Author author = AuthorMapper.toEntity(request);
		Author createdAuthor = this.authorRepository.save(author);
		return AuthorMapper.toResponse(createdAuthor);
	}
}
