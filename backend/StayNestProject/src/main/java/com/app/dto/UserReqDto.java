package com.app.dto;

import org.hibernate.validator.constraints.Length;


import com.app.entities.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReqDto {
	
	@NotBlank(message = "first name is required")
	@Length(min = 5, max = 20, message = "invalid length of firstname")
	private String firstName;
	@NotBlank(message = "last name is required")
	private String lastName;
	@NotBlank
	@Email(message = "invalid email format")
	private String email;
	@Pattern
	(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[#@$*]).{5,20})", 
	message = "Invalid password format")
	private String password;
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phone;
	@NotNull
	private UserRole role;
}
