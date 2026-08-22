package org.library.config;

import lombok.RequiredArgsConstructor;
import org.library.entity.*;
import org.library.enums.Role;
import org.library.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TestDataConfig
{
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;
	private final BorrowRecordRepository borrowRecordRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	public ApplicationRunner dataSeeder()
	{
		return args -> {
			// Users
			User admin = userRepository.findByEmail("admin@library.com")
					.orElseGet(() -> userRepository.save(User.builder()
							.name("Admin User")
							.role(Role.ADMIN)
							.email("admin@library.com")
							.password(passwordEncoder.encode("admin123"))
							.build()));

			User user1 = userRepository.findByEmail("john@library.com")
					.orElseGet(() -> userRepository.save(User.builder()
							.name("John Doe")
							.role(Role.USER)
							.email("john@library.com")
							.password(passwordEncoder.encode("john123"))
							.build()));

			User user2 = userRepository.findByEmail("jane@library.com")
					.orElseGet(() -> userRepository.save(User.builder()
							.name("Jane Smith")
							.role(Role.USER)
							.email("jane@library.com")
							.password(passwordEncoder.encode("jane123"))
							.build()));

			// Categories
			Category fiction = categoryRepository.findByName("Fiction")
					.orElseGet(() -> categoryRepository.save(new Category(null, "Fiction")));

			Category science = categoryRepository.findByName("Science")
					.orElseGet(() -> categoryRepository.save(new Category(null, "Science")));

			Category history = categoryRepository.findByName("History")
					.orElseGet(() -> categoryRepository.save(new Category(null, "History")));

			Category technology = categoryRepository.findByName("Technology")
					.orElseGet(() -> categoryRepository.save(new Category(null, "Technology")));

			// Authors
			Author orwell = authorRepository.findByName("George Orwell")
					.orElseGet(() -> authorRepository.save(new Author(null, "George Orwell")));

			Author hawking = authorRepository.findByName("Stephen Hawking")
					.orElseGet(() -> authorRepository.save(new Author(null, "Stephen Hawking")));

			Author harari = authorRepository.findByName("Yuval Noah Harari")
					.orElseGet(() -> authorRepository.save(new Author(null, "Yuval Noah Harari")));

			Author brooks = authorRepository.findByName("David Brooks")
					.orElseGet(() -> authorRepository.save(new Author(null, "David Brooks")));

			// Books
			Book book1 = bookRepository.findByTitle("1984")
					.orElseGet(() -> bookRepository.save(Book.builder()
							.title("1984")
							.description("A dystopian social science fiction novel and cautionary tale.")
							.availableCopies(5L)
							.category(fiction)
							.authors(List.of(orwell))
							.build()));

			Book book2 = bookRepository.findByTitle("A Brief History of Time")
					.orElseGet(() -> bookRepository.save(Book.builder()
							.title("A Brief History of Time")
							.description("A landmark volume in science writing.")
							.availableCopies(3L)
							.category(science)
							.authors(List.of(hawking))
							.build()));

			Book book3 = bookRepository.findByTitle("Sapiens: A Brief History of Humankind")
					.orElseGet(() -> bookRepository.save(Book.builder()
							.title("Sapiens: A Brief History of Humankind")
							.description("A narrative of humanity's creation and evolution.")
							.availableCopies(4L)
							.category(history)
							.authors(List.of(harari))
							.build()));

			Book book4 = bookRepository.findByTitle("Animal Farm")
					.orElseGet(() -> bookRepository.save(Book.builder()
							.title("Animal Farm")
							.description("An allegorical novella about revolution.")
							.availableCopies(2L)
							.category(fiction)
							.authors(List.of(orwell))
							.build()));

			// Borrow Records
			borrowRecordRepository.findOneByUserAndBook(user1, book1)
					.orElseGet(() -> borrowRecordRepository.save(BorrowRecord.builder()
							.user(user1)
							.book(book1)
							.borrowDate(LocalDate.now().minusDays(10))
							.dueDate(LocalDate.now().plusDays(4))
							.returnDate(null)
							.build()));

			borrowRecordRepository.findOneByUserAndBook(user2, book3)
					.orElseGet(() -> borrowRecordRepository.save(BorrowRecord.builder()
							.user(user2)
							.book(book3)
							.borrowDate(LocalDate.now().minusDays(5))
							.dueDate(LocalDate.now().plusDays(9))
							.returnDate(null)
							.build()));
		};
	}
}
