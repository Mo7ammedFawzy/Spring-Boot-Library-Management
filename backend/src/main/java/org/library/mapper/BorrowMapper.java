package org.library.mapper;

import org.library.dto.BorrowResponse;
import org.library.entity.BorrowRecord;

public class BorrowMapper
{
	public static BorrowResponse toResponse(BorrowRecord entity)
	{
		return new BorrowResponse(entity.getId(), UserMapper.toResponse(entity.getUser()), BookMapper.toResponse(entity.getBook()),
				entity.getBorrowDate(), entity.getDueDate(), entity.getReturnDate());
	}
}
