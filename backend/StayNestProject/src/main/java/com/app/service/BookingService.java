	package com.app.service;
	
	import java.util.List;
	
	import com.app.dto.ApiResponse;
	import com.app.dto.BookingReqDto;
	import com.app.dto.BookingRespDto;
	
	public interface BookingService {
	
	
		ApiResponse deleteBooking(Long bookingId);
	
		List<BookingRespDto> getActiveBooking();
	
		ApiResponse updateBooking(Long bookingId, BookingReqDto dto);
	
		BookingRespDto getBokingDetailsById(Long bookingId);

		ApiResponse addBooking(Long propertyId, BookingReqDto dto);
		
	
	}
