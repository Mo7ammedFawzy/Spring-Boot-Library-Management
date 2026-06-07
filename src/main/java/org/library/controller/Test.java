package org.library.controller;

import org.library.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class Test
{

	@GetMapping
	public ResponseEntity<ApiResponse<String>> getTest()
	{
		ApiResponse<String> response = ApiResponse.ok("Hello World");
		return ResponseEntity.ok(response);
	}
}