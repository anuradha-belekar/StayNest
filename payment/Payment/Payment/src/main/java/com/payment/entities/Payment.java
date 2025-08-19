package com.payment.entities;

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
@Table(name = "payments")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Payment extends BaseEntity{
	@Column(name = "booking_id",nullable = false)
	private Long bookingId;
	@Column(name = "stripe_payment_id",length = 100,nullable = false)
	private String stripePaymentId;
	@Column(name = "amount",nullable = false)
	private double amount;
	@Enumerated(EnumType.STRING)
	@Column(name = "status",nullable = false)
	private PaymentStatus status;
	public Payment(Long bookingId, String stripePaymentId, double amount, PaymentStatus status) {
		super();
		this.bookingId = bookingId;
		this.stripePaymentId = stripePaymentId;
		this.amount = amount;
		this.status = status;
	}
	
	
}
