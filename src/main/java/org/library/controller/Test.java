package org.library.controller;

import org.library.core.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class Test
{

	@GetMapping
	public ResponseEntity<ApiResponse<String>> getTest()
	{
		ApiResponse<String> response = ApiResponse.<String>create().success().data("Hello World!").build();
		return ResponseEntity.ok(response);
	}
}