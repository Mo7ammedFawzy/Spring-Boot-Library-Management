package org.library.dto;

import java.util.List;

public record BookResponse(Long id, String title, String description, Long availableCopies, CategoryResponse category, List<AuthorResponse> authors)
{
}
