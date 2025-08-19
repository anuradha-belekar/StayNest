package com.app.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Otp;
import com.app.entities.User;

public interface OtpDao extends JpaRepository<Otp, Long>{

	Optional<Otp> findByOtpValueAndUserAndIsUsedFalseAndExpiryTimeAfter(String otpValue, User user, LocalDateTime currentTime);

}
