package com.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.app.entities.BookingStatus;
import com.app.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BookingRespDto extends BaseDto{
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private double totalPrice;
	
	private BookingStatus bookingStatus;
	
	private int numberOfGuests;
	
	private Long customerId;
	
	private Long propertyId;
	 
	private String propertyName;
	
	private Long paymentId;
	
	private PaymentStatus paymentStatus;
	
	private List<PropertyBlockoutDto> blockoutDates = new ArrayList<>();
	   
	private List<PropertyImageRespDto> images = new ArrayList<>();
}
