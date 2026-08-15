package org.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.library.dto.*;
import org.library.payload.ApiResponse;
import org.library.service.CategoryService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController
{
	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories()
	{
		return ApiResponse.okResponse(this.categoryService.getCategories());
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request)
	{
		CategoryResponse response = this.categoryService.createCategory(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id)
	{
		this.categoryService.deleteCategory(id);
		return ApiResponse.okResponse("Category deleted successfully!");
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request)
	{
		CategoryResponse categoryResponse = this.categoryService.updateCategory(id, request);
		return ApiResponse.okResponse(categoryResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id)
	{
		CategoryResponse categoryResponse = this.categoryService.getCategoryById(id);
		return ApiResponse.okResponse(categoryResponse);
	}
}
