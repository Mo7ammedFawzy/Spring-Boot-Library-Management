package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.core.ApiResponse;
import org.library.dto.*;
import org.library.service.impl.CategoryServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController
{
	private final CategoryServiceImp categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories()
	{
		return ApiResponse.okResponse(this.categoryService.getCategories());
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request){
		return ApiResponse.okResponse(this.categoryService.createCategory(request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id){
		this.categoryService.deleteCategory(id);
		return  ApiResponse.okResponse("Category deleted successfully!");
	}
}
