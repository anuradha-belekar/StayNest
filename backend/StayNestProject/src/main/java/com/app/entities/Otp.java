package com.app.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "otps")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "user")
public class Otp extends BaseEntity{
	@Column(name = "otp_value", length = 6, nullable = false)
	private String otpValue;
	@Column(name = "expiry_time", nullable = false)
	private LocalDateTime expiryTime;
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(name = "is_used", nullable = false)
	private boolean isUsed=false;
	
	
	
	public Otp(String otpValue, LocalDateTime localDateTime, User user) {
		super();
		this.otpValue = otpValue;
		this.expiryTime = localDateTime;
		this.user = user;
		
	}
	
	
}
