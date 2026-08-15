package org.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.library.dto.BorrowResponse;
import org.library.entity.*;
import org.library.exception.*;
import org.library.mapper.BorrowMapper;
import org.library.repository.*;
import org.library.service.BorrowService;
import org.library.utils.CurrentUserService;
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

	@Override
	public BorrowResponse borrow(Long id)
	{
		Book book = bookRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.create(Book.class, id));
		if (!checkIfBookIsAvailable(book))
			throw new BookUnavailableException();
		User user = CurrentUserService.getCurrentUser();
		LocalDate now = LocalDate.now();
		Long currentBookCopies = book.getAvailableCopies();
		book.setAvailableCopies(currentBookCopies - 1);
		bookRepository.save(book);
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

	private boolean checkIfBookIsAvailable(Book book)
	{
		if (ObjectUtils.isEmpty(book))
			return false;
		else if (book.getAvailableCopies() < 1)
			return false;
		else
			return true;
	}
}
