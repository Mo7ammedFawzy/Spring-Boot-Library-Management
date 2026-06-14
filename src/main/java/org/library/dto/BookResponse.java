package org.library.dto;

import lombok.*;
import org.library.entity.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse
{
	private Long id;
	private String title;
	private String description;
	private Long availableCopies;
	private Category category;
}
