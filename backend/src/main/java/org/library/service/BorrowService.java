package org.library.service;

import org.library.dto.BorrowResponse;

import java.util.List;

public interface BorrowService
{
	BorrowResponse borrow(Long id);

	List<BorrowResponse> getAllBorrows();
}
