package org.library.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequest(@NotBlank String name)
{
}
