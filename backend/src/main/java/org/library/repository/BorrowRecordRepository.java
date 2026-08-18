package org.library.repository;

import org.library.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long>
{
	Optional<BorrowRecord> findOneByUserAndBook(User user, Book book);
}
