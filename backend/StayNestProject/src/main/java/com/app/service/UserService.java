package com.app.service;

import java.util.List;

import com.app.dto.ApiResponse;
import com.app.dto.SignUpDto;
import com.app.dto.UserRespDto;

public interface UserService {

	UserRespDto signUp(SignUpDto dto);

	List<UserRespDto> getAllUsers();

	ApiResponse deleteUser(Long userId);

	Object verifyUserOtp(String email, String otpValue);
	
	// method for admin role transfer
    ApiResponse transferAdminRole(Long newAdminId);  


}
