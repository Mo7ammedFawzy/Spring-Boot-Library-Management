package org.library.service;

import org.library.dto.BorrowResponse;

import java.util.List;

public interface BorrowService
{
	BorrowResponse borrow(Long bookId, Long userId);

	List<BorrowResponse> getAllBorrows();

	BorrowResponse returnBook(Long borrowId);
}
