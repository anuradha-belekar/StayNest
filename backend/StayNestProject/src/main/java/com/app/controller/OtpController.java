package com.app.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.OtpRequestDto;
import com.app.service.OtpService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/otp")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OtpController {
	
	private final OtpService otpService;
	
	
	@PostMapping("/generate")
	@Operation(description = "Generate and send OTP to user email")
	public ResponseEntity<?> generateOtp(@RequestBody @Valid OtpRequestDto dto){
		return ResponseEntity.status(HttpStatus.OK).body(otpService.generateAndSendOtp(dto.getEmail()));
	}
	
	@Hidden
	@PostMapping("/verify")
    @Operation(description = "Verify OTP for user")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody @Valid OtpRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(otpService.verifyOtp(dto.getEmail(), dto.getOtpValue()));
    }

}
