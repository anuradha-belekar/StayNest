package com.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReqDto {
	
	@Min(value = 1,message = "booking id must be greate than 0")
	private Long bookingId;
	
	@Min(value = 1,message = "amount must be greater than 0")
	private double amount;
	
	@NotBlank(message = "payment method id is required")
	private String paymentMethodId;
	
	

}
