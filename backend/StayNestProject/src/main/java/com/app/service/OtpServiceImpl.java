package com.app.service;

import java.security.SecureRandom;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.app.custom_exceptions.InvalidInputException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.OtpDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.entities.Otp;
import com.app.entities.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 5;

    private final UserDao userDao;
    private final OtpDao otpDao;
    private final JavaMailSender mailSender;

    @Override
    public ApiResponse generateAndSendOtp(String email) {
        // Find user by email
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Generate 6-digit OTP
        String otpValue = generateOtp();

        // Save OTP to database
        Otp otp = new Otp(otpValue, LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES), user);
        otpDao.save(otp);

        // Send OTP via email
        sendOtpEmail(email, otpValue);

        // Return success response
        return new ApiResponse("OTP sent to " + email);
    }

    @Override
    public ApiResponse verifyOtp(String email, String otpValue) {
        // Find user by email
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Find valid OTP
        Otp otp = otpDao.findByOtpValueAndUserAndIsUsedFalseAndExpiryTimeAfter(otpValue, user, LocalDateTime.now())
                .orElseThrow(() -> new InvalidInputException("Invalid or expired OTP"));

        // Mark OTP as used
        otp.setUsed(true);
        otpDao.save(otp);

        return new ApiResponse("OTP verified successfully");
    }

    private void sendOtpEmail(String email, String otpValue) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your StayNest OTP");
        message.setText("Your OTP for StayNest is: " + otpValue + "\nThis OTP is valid for " + OTP_VALIDITY_MINUTES + " minutes.");
        mailSender.send(message);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}