package org.library.dto;

import java.time.LocalDate;

public record BorrowResponse(Long id, UserResponse userResponse, BookResponse bookResponse, LocalDate borrowDate, LocalDate dueDate,
                             LocalDate returnDate)
{
}
