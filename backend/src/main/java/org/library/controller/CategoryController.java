package org.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.entity.Category;
import org.library.exception.ResourceNotFoundException;
import org.library.mapper.CategoryMapper;
import org.library.payload.ApiResponse;
import org.library.repository.CategoryRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController
{
	private final CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories()
	{
		List<CategoryResponse> response = this.categoryRepository.findAll()
				.stream()
				.map(CategoryMapper::toResponse)
				.toList();
		return ApiResponse.okResponse(response);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request)
	{
		Category category = CategoryMapper.toEntity(request);
		Category createdCategory = this.categoryRepository.save(category);
		CategoryResponse response = CategoryMapper.toResponse(createdCategory);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id)
	{
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.create(Category.class, id));
		this.categoryRepository.delete(category);
		return ApiResponse.okResponse("Category deleted successfully!");
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request)
	{
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.create(Category.class, id));
		category.setName(request.name());
		Category updatedCategory = this.categoryRepository.save(category);
		CategoryResponse response = CategoryMapper.toResponse(updatedCategory);
		return ApiResponse.okResponse(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id)
	{
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> ResourceNotFoundException.create(Category.class, id));
		CategoryResponse response = CategoryMapper.toResponse(category);
		return ApiResponse.okResponse(response);
	}
}
