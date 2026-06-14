package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.core.ResourceNotFoundException;
import org.library.dto.*;
import org.library.entity.Category;
import org.library.mapper.CategoryMapper;
import org.library.repository.CategoryRepository;
import org.library.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService
{
	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryResponse> getCategories()
	{
		List<Category> categories = this.categoryRepository.findAll();
		return categories.stream().map(CategoryMapper::toResponse).toList();
	}

	@Override
	public CategoryResponse createCategory(CategoryRequest request)
	{
		Category category = CategoryMapper.toEntity(request);
		Category createdCategory = this.categoryRepository.save(category);
		return CategoryMapper.toResponse(createdCategory);
	}

	@Override
	public void deleteCategory(Long id)
	{
		Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
		this.categoryRepository.delete(category);
	}
}
