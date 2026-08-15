package org.library.dto;

import org.library.entity.Author;
import org.library.entity.Category;

import java.util.List;

public record BookResponse(Long id, String title, String description, Long availableCopies, Category category, List<Author> authors)
{
}
