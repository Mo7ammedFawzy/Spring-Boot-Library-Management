package org.library.service;

import org.library.dto.*;

import java.util.List;

public interface CategoryService
{
	List<CategoryResponse> getCategories();

	CategoryResponse createCategory(CategoryRequest request);

	void deleteCategory(Long id);
	CategoryResponse updateCategory(Long id, CategoryRequest request);

	CategoryResponse getCategoryById(Long id);
}
