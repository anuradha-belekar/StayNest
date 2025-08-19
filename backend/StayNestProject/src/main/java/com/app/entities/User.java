package com.app.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

public class User extends BaseEntity implements UserDetails{
	
	@Column(name = "first_name",length = 30,nullable = false)
	private String firstName;
	@Column(name = "last_name",length = 30)
	private String lastName;
	@Column(length = 30,unique = true,nullable = false)
	private String email;
	@Column(length = 300,nullable = false)
	private String password;
	@Column(length=10,nullable = false)
	private String phone;
	@Enumerated(EnumType.STRING)
	@Column(length = 30,name = "user_role")
	private UserRole role;
	@Column(name = "status", nullable = false)
    private boolean status = true; // Soft delete field
	@Column(name = "pending_verification", nullable = false)
    private boolean pendingVerification = true; // New field for OTP verification
	
	//for retrieving details of user
	public User(String firstName, String lastName, String email, String password, String phone, UserRole role,
			boolean status) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.status = status;
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
		this.status = true;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}
	
	
	@Override
    public boolean isEnabled() {
        return this.status && !this.pendingVerification; // Tie to soft delete status
    }



	public User(String firstName, String lastName, String email, String password, String phone, UserRole role,
			boolean status, boolean pendingVerification) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.status = status;
		this.pendingVerification = pendingVerification;
	}
	
	
}
