package org.library.repository;

import org.library.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long>
{
	BorrowRecord findOneByUserAndBook(User user, Book book);
}
