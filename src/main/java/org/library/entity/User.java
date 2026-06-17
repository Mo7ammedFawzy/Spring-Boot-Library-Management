package org.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.library.enums.Role;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;
	@NotBlank
	private String password;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;
}
