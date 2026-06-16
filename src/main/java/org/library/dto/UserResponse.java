package org.library.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse
{
	private Long id;
	private String name;
	private String email;
}
