package com.payment.dto;

import com.payment.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRespDto extends BaseDto{
	
	private Long bookingId;
	private String stripePaymentId;
	private double amount;
	private PaymentStatus status;
	private String message;
	public PaymentRespDto(Long bookingId, String stripePaymentId, double amount, PaymentStatus status) {
		super();
		this.bookingId = bookingId;
		this.stripePaymentId = stripePaymentId;
		this.amount = amount;
		this.status = status;
		this.message = message;
	}
	
	
	

}
