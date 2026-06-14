package org.library.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryRequest
{
	@NotNull
	@NotBlank
	private String name;
}
