package org.library.mapper;

import org.library.dto.*;
import org.library.entity.Category;

public class CategoryMapper
{
	public static CategoryResponse toResponse(Category category)
	{

		return new CategoryResponse(category.getId(), category.getName());
	}

	public static Category toEntity(CategoryRequest request)
	{
		Category entity = new Category();
		entity.setName(request.getName());
		return entity;
	}
}
