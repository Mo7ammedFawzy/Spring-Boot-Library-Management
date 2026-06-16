package org.library.repository;

import org.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmail(String email);
}
