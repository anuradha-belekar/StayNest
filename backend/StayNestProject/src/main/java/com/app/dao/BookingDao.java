package com.app.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Booking;
import com.app.entities.Property;

public interface BookingDao extends JpaRepository<Booking, Long>{

	boolean existsByPropertyAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(Property property,
			LocalDate checkInDate, LocalDate checkOutDate);
	
	List<Booking> findByIsActiveTrue();

}
