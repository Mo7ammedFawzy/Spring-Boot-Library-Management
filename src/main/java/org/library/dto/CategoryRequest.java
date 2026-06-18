package org.library.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryRequest
{
	@NotBlank
	private String name;
}
