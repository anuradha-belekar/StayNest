package com.app.dto;

import com.app.entities.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRespDto extends BaseDto{
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;
	
	private UserRole role;
	
	private boolean status;

}
