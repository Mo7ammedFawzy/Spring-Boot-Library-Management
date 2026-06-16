package org.library.service;

import org.library.dto.*;

import java.util.List;

public interface AuthorService
{
	List<AuthorResponse> getAllAuthors();

	AuthorResponse createAuthor(AuthorRequest request);
}
