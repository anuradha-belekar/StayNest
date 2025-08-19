package com.app.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.app.client.PaymentClient;
import com.app.custom_exceptions.InvalidInputException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.BookingDao;
import com.app.dao.PropertyDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.dto.BookingReqDto;
import com.app.dto.BookingRespDto;
import com.app.dto.PaymentReqDto;
import com.app.entities.Booking;
import com.app.entities.BookingStatus;
import com.app.entities.PaymentStatus;
import com.app.entities.Property;
import com.app.entities.PropertyBlockout;
import com.app.entities.User;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingDao bookingDao;
	private final UserDao userDao;
	private final PropertyDao propertyDao;
	private final ModelMapper modelMapper;
	private PaymentClient paymentClient;
	
	@Override
	public ApiResponse addBooking(Long propertyId, BookingReqDto dto) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	    User customer = userDao.findByEmail(email)
	        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
	    Property property = propertyDao.findById(propertyId)
	        .orElseThrow(() -> new ResourceNotFoundException("Invalid property id"));

	    // Validate dates
	    if (dto.getCheckInDate().isAfter(dto.getCheckOutDate())) {
	        throw new InvalidInputException("Check-in date must be before check-out date");
	    }
	    if (dto.getCheckInDate().isBefore(LocalDate.now())) {
	        throw new InvalidInputException("Check-in date should not be a past date");
	    }

	    // Check availability
	    if (!property.isAvailablity()) {
	        throw new InvalidInputException("Property is not available for booking");
	    }

	    // Check blockout
	    boolean hasBlockout = property.getBlockoutDates().stream()
	        .anyMatch(blockout -> !dto.getCheckOutDate().isBefore(blockout.getBlockOutDate())
	            && !dto.getCheckInDate().isAfter(blockout.getBlockOutDate()));
	    if (hasBlockout) {
	        throw new InvalidInputException("Property is unavailable for selected dates");
	    }

	    //Create booking and calculate total price BEFORE creating payment request
	    Booking booking = modelMapper.map(dto, Booking.class);
	    booking.setCustomer(customer);
	    booking.setProperty(property);
	    booking.setBookingStatus(BookingStatus.PENDING);
	    booking.setPaymentStatus(PaymentStatus.PENDING);

	    long nights = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
	    double totalPrice = property.getPrice() * nights;
	    booking.setTotalPrice(totalPrice);

	    Booking savedBooking = bookingDao.save(booking);

	    // Blockout dates
	    List<LocalDate> bookingDates = Stream.iterate(dto.getCheckInDate(),
	        date -> !date.isAfter(dto.getCheckOutDate()),
	        date -> date.plusDays(1)).collect(Collectors.toList());
	    for (LocalDate date : bookingDates) {
	        property.getBlockoutDates().add(new PropertyBlockout(date, property, savedBooking));
	    }
	    propertyDao.save(property);  // Save updated property blockouts

	    //Create PaymentReqDto from the saved booking
	    PaymentReqDto paymentDto = new PaymentReqDto();
	    paymentDto.setBookingId(savedBooking.getId());
	    paymentDto.setAmount(savedBooking.getTotalPrice());  // ✅ Use saved totalPrice
	    paymentDto.setPaymentMethodId(dto.getPaymentMethodId());

	    String jwt = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

	    try {
	        ApiResponse paymentResponse = paymentClient.makePayment(paymentDto, jwt);
	        Long paymentId = extractPaymentIdFromResponse(paymentResponse);

	        savedBooking.setPaymentId(paymentId);
	        savedBooking.setPaymentStatus(PaymentStatus.SUCCESS);
	        bookingDao.save(savedBooking);

	        return new ApiResponse("Booking created with id " + savedBooking.getId() + " and payment ID " + paymentId);
        } catch (HttpClientErrorException e) {
        	savedBooking.setPaymentStatus(PaymentStatus.FAILED);
            bookingDao.save(savedBooking);
            throw new InvalidInputException("Payment failed: " + e.getMessage());
        } catch (Exception e) {
        	savedBooking.setPaymentStatus(PaymentStatus.FAILED);
            bookingDao.save(savedBooking);
            throw new InvalidInputException("Unexpected error during payment: " + e.getMessage());
        }
    }

    // Helper method to extract payment ID from response
    private Long extractPaymentIdFromResponse(ApiResponse response) {
        String message = response.getMessage();
        // Extract ID from "Payment created successfully with ID: X"
        String idStr = message.replaceAll("[^0-9]", "");
        return Long.parseLong(idStr);
    }
	@Override
	public ApiResponse deleteBooking(Long bookingId) {
		// TODO Auto-generated method stub
		//Authenticated user
				String email=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
				
				//validate User
				
		User customer=userDao.findByEmail(email)
				.orElseThrow(()->new ResourceNotFoundException("User not found with email:" + email));
		Booking booking=bookingDao.findById(bookingId)
				.orElseThrow(()->new ResourceNotFoundException("Invalid booking Id"));
		Property property=booking.getProperty();
		property.getBlockoutDates().removeIf(blockout->
		blockout.getBooking()!=null && blockout.getBooking().getId().equals(bookingId));
		booking.setActive(false);
		bookingDao.save(booking);
		return new ApiResponse("booking deleted with id"+bookingId);
	}


	@Override
	public List<BookingRespDto> getActiveBooking() {
		// TODO Auto-generated method stub
		//Authenticated user
		String email=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		//validate User
		User customer=userDao.findByEmail(email)
		.orElseThrow(()->new ResourceNotFoundException("User not found with email:" + email));

		System.out.println(bookingDao.findByIsActiveTrue());
		System.out.println("Current logged in customer email: " + customer.getEmail());
		return bookingDao.findByIsActiveTrue().stream()
			.filter(booking->booking.getCustomer().getEmail().equals(customer.getEmail()))
				
			.map(booking -> {
			    BookingRespDto dto = modelMapper.map(booking, BookingRespDto.class);
			    System.out.println("Mapped DTO: " + dto);
			    return dto;
			})
	
			.toList();
		
	}


	@Override
	public ApiResponse updateBooking(Long bookingId, BookingReqDto dto) {
		// TODO Auto-generated method stub
		//Authenticated user
				String email=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
				
				//validate User
				
				  User customer=userDao.findByEmail(email).orElseThrow(()->new
						  ResourceNotFoundException("User not found with email:" + email));
				 
				  //validate booking
				  Booking booking=bookingDao.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
				
				  //validate property and get its id
				Property property=booking.getProperty();
				
				//validate check in and check out dates
				if(dto.getCheckInDate().isAfter(dto.getCheckOutDate())) {
					throw new InvalidInputException("check in date must be before check out date");
				}
				if(dto.getCheckInDate().isBefore(LocalDate.now())) {
					throw new InvalidInputException("check in date should not be past date");
				}
				
				//remove old blockout dates
				property.getBlockoutDates().removeIf(b->b.getBooking()!=null && b.getBooking().getId().equals(bookingId));
				
				//check for blockout dates(include booked dates and owner blockout)
				//check if any blockout dates fall within new booking range
				boolean hasBlockout = property.getBlockoutDates().stream()
						.anyMatch((blockout)->!dto.getCheckOutDate().isBefore(blockout.getBlockOutDate())&&
											  !dto.getCheckInDate().isBefore(blockout.getBlockOutDate()));
				if(hasBlockout) {
					throw new InvalidInputException("property is unavailable for selected dates");
				}
				
				//update booking fields
				booking.setCheckInDate(dto.getCheckInDate());
				booking.setCheckOutDate(dto.getCheckOutDate());
				booking.setNumberOfGuests(dto.getNumberOfGuests());
				
				//calculate total price based on property price and no.of nights
				long nights=ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
				booking.setTotalPrice(property.getPrice()*nights);
				booking.setUpdatedOn(LocalDate.now());
				
				
				
				
				//create property blockout entries for each date in booking range
				List<LocalDate> bookingDates =Stream.iterate(dto.getCheckInDate(),
						date->date.isBefore(dto.getCheckOutDate()) ||
						      date.isEqual(dto.getCheckOutDate()),
						      date->date.plusDays(1)).collect(Collectors.toList());
				
				for(LocalDate date:bookingDates) {
					PropertyBlockout blockout=new PropertyBlockout(date, property,booking);
					property.getBlockoutDates().add(blockout);
				}
				
				propertyDao.save(property);//save blockout entries
				//save the booking
				Booking saveBooking =bookingDao.save(booking);
				return new ApiResponse("Booking updated with id" + saveBooking.getId());
			
	}


	@Override
	public BookingRespDto getBokingDetailsById(Long bookingId) {
		// TODO Auto-generated method stub
		//Authenticated user
				String email=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
				
				//validate User
				User customer=userDao.findByEmail(email)
				.orElseThrow(()->new ResourceNotFoundException("User not found with email:" + email));
				
				//validate booking id
				Booking booking=bookingDao.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
				
				//for soft delete
				if(!booking.isActive()) {
					throw new InvalidInputException("booking is not available");
				}
				
				BookingRespDto dto=modelMapper.map(booking, BookingRespDto.class);
				
				//set paymentid if exists
				if(booking.getPaymentId()!=null) {
					dto.setPaymentId(booking.getPaymentId());
				}
				
				
		return dto;
	}
	
	
	
}
