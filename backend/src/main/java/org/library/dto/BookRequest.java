package org.library.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BookRequest(@NotBlank String title, @Size(min = 10) String description, @NotNull @Min(0) Long availableCopies,
                          @NotNull Long categoryId, @NotEmpty List<@NotNull Long> authorIds)
{
}
