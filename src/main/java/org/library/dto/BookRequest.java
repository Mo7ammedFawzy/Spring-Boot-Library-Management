package org.library.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookRequest
{
	@NotBlank
	private String title;
	private String description;
	@NotNull
	private Long availableCopies;
}