package com.app.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.entities.User;
import com.app.entities.UserRole;

public interface UserDao extends JpaRepository<User, Long>{

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);
	
	 @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.status = true")
	    long countByRole(UserRole role); // Count users with a specific role


}
