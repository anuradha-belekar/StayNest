package com.app.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class BookingReqDto extends BaseDto{
	
	@NotNull
	private LocalDate checkInDate;
	
	@NotNull
	private LocalDate checkOutDate;
	
	@Min(1)
	private int numberOfGuests;
	
	
	@NotBlank(message = "payment method id is required")
	private String paymentMethodId;
	
}
