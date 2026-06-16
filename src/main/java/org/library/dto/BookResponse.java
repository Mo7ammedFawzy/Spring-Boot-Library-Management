package org.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.library.entity.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

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
	private List<Author> authors;
}
