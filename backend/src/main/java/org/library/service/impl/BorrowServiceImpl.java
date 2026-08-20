package org.library.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.library.dto.BorrowResponse;
import org.library.entity.*;
import org.library.exception.*;
import org.library.mapper.BorrowMapper;
import org.library.repository.*;
import org.library.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService
{

	private final BookRepository bookRepository;
	private final BorrowRecordRepository borrowRecordRepository;
	private final UserContext userContext;

	@Override
	@Transactional
	public BorrowResponse borrow(Long bookId)
	{
		Book book = bookRepository.findById(bookId).orElseThrow(() -> ResourceNotFoundException.create(Book.class, bookId));
		if (!book.isAvailable())
			throw new BookUnavailableException();
		User user = userContext.getCurrentUser();
		book.decreaseAvailableCopies();
		bookRepository.save(book);
		LocalDate now = LocalDate.now();
		BorrowRecord borrowRecord = BorrowRecord.builder().user(user).book(book).borrowDate(now).dueDate(now.plusDays(14)).build();
		borrowRecordRepository.save(borrowRecord);
		return BorrowMapper.toResponse(borrowRecord);
	}

	@Override
	public List<BorrowResponse> getAllBorrows()
	{
		List<BorrowRecord> records = borrowRecordRepository.findAll();
		return records.stream().map(BorrowMapper::toResponse).toList();
	}

	@Override
	@Transactional
	public BorrowResponse returnBook(Long borrowId)
	{
		BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
				.orElseThrow(() -> ResourceNotFoundException.create(BorrowRecord.class, borrowId));
		if (!ObjectUtils.isEmpty(borrowRecord.getReturnDate()))
			throw new RuntimeException("Book Already Returned");

		borrowRecord.setReturnDate(LocalDate.now());
		borrowRecordRepository.save(borrowRecord);

		Book book = borrowRecord.getBook();
		book.increaseAvailableCopies();

		return BorrowMapper.toResponse(borrowRecord);
	}

}
