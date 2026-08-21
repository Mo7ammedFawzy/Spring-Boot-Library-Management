package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.dto.BorrowResponse;
import org.library.payload.ApiResponse;
import org.library.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController
{
	private final BorrowService borrowService;

	@PostMapping("/{bookId}")
	public ResponseEntity<ApiResponse<BorrowResponse>> borrow(@PathVariable Long bookId, @RequestParam(required = false) Long userId)
	{
		BorrowResponse borrowResponse = borrowService.borrow(bookId, userId);
		return ApiResponse.okResponse(borrowResponse);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<BorrowResponse>>> getAllBorrows()
	{
		List<BorrowResponse> response = borrowService.getAllBorrows();
		return ApiResponse.okResponse(response);
	}

	@PostMapping("/{borrowId}/return")
	public ResponseEntity<ApiResponse<BorrowResponse>> returnBook(@PathVariable Long borrowId){
		BorrowResponse response = borrowService.returnBook(borrowId);
		return ApiResponse.okResponse(response);
	}
}
