package com.app.service;

import com.app.dto.ApiResponse;

public interface OtpService {
	ApiResponse generateAndSendOtp(String email);
    ApiResponse verifyOtp(String email, String otpValue);
}
