package org.library.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BookRequest
{
	@NotBlank
	private String title;
	@Size(min = 10)
	private String description;
	@NotNull
	@Min(0)
	private Long availableCopies;
	@NotNull
	private Long categoryId;
	@NotEmpty
	private List<@NotNull Long> authorIds;
}