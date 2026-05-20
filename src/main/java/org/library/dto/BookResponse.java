package org.library.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse
{
	private Long id;
	private String title;
	private String description;
	private Long availableCopies;
}
