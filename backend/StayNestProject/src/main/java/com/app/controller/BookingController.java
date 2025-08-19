package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.BookingReqDto;
import com.app.dto.BookingRespDto;
import com.app.service.BookingService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class BookingController {
	private final BookingService bookingService;
	@PostMapping("/properties/{propertyId}/bookings")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "create a new booking")
	public ResponseEntity<?> createBooking(
			@PathVariable @Min(1) Long propertyId,
			@RequestBody @Valid BookingReqDto dto,@RequestHeader("Authorization")String authHeader)
	{
		System.out.println("in add booking"+dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.addBooking(propertyId, dto));
	}
	
	@DeleteMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "delete a booking")
	public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId){
		System.out.println("in delete booking"+bookingId);
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.deleteBooking(bookingId));
	}
	
	@GetMapping("/bookings")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "Get all active bookings for authenticated user")
	public  ResponseEntity<List<BookingRespDto>> getActiveBooking(){
		System.out.println("in get all active bookings");
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.getActiveBooking());
	}
	
	@GetMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "Get details of active booking requested by authenticated user")
	public ResponseEntity<?> getBookingDetails(@PathVariable Long bookingId){
		System.out.println("in get by id booking");
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBokingDetailsById(bookingId));
	}
	
	@PutMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	@Operation(description = "update bookings for authenticated user")
	public ResponseEntity<?> updateBooking(@RequestBody BookingReqDto dto,@PathVariable Long bookingId){
		System.out.println("in update booking"+bookingId);
		return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingId,dto));
	}
	
	
	
}
