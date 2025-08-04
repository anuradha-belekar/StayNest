package com.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
@Setter
@ToString

public class User extends BaseEntity{
	
	@Column(name = "first_name",length = 30,nullable = false)
	private String firstName;
	@Column(name = "last_name",length = 30)
	private String lastName;
	@Column(length = 30,unique = true,nullable = false)
	private String email;
	@Column(length = 20,nullable = false)
	private String password;
	@Column(length=10,nullable = false)
	private String phone;
	@Enumerated(EnumType.STRING)
	@Column(length = 30,name = "user_role")
	private UserRole role;
	
	
	//for retrieving details of user
	public User(String firstName, String lastName, String email, String phone, UserRole role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.role = role;
	}

	//for signup
	public User(String firstName, String lastName, String email, String password, String phone, UserRole role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
	}

	
	
}
