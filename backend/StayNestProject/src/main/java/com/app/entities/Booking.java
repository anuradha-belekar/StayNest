package com.app.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "bookings")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"property","customer"})
public class Booking extends BaseEntity{
	@Column(name = "check_in_date",nullable = false)
	private LocalDate checkInDate;
	
	@Column(name = "check_out_date",nullable = false)
	private LocalDate checkOutDate;
	
	@Column(name = "total_price")
	private double totalPrice;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status")
	private BookingStatus bookingStatus;
	
	@Column(name = "number_of_guests",nullable = false)
	private int numberOfGuests;
	
	@ManyToOne
	@JoinColumn(name = "customer_id",nullable = false)
	private User customer;
	
	@ManyToOne
	@JoinColumn(name = "property_id",nullable = false)
	private Property property;
	
	@Column(name = "payment_id",length = 50)
	private Long paymentId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status")
	private PaymentStatus paymentStatus;
	
	@Column(name = "isActive")
	private boolean isActive = true; //Added for soft delete

	public Booking(LocalDate checkInDate, LocalDate checkOutDate, double totalPrice, BookingStatus bookingStatus,
			int numberOfGuests, User customer, Property property, Long paymentId, PaymentStatus paymentStatus,
			boolean isActive) {
		super();
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.totalPrice = totalPrice;
		this.bookingStatus = bookingStatus;
		this.numberOfGuests = numberOfGuests;
		this.customer = customer;
		this.property = property;
		this.paymentId = paymentId;
		this.paymentStatus = paymentStatus;
		this.isActive = isActive;
	}
	
	

}
