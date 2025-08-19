package com.app;

import org.modelmapper.Conditions;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.dto.BookingRespDto;
import com.app.entities.Booking;

@SpringBootApplication
public class StayNestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StayNestApplication.class, args);
	}

	
	@Bean
	ModelMapper modelMapper() {
		System.out.println("creating model mapper");
		ModelMapper mapper= new ModelMapper();
		//to transfer only properties matching by name 
		mapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		//transfer not null props
		.setPropertyCondition(Conditions.isNotNull());
		mapper.typeMap(Booking.class, BookingRespDto.class)
        .addMappings(mapping -> {
            mapping.map(src -> src.getCustomer().getId(), BookingRespDto::setCustomerId);
            mapping.map(src -> src.getProperty().getId(), BookingRespDto::setPropertyId);
            mapping.map(src -> src.getProperty().getPropertyName(), BookingRespDto::setPropertyName);
            mapping.map(src -> src.getProperty().getBlockoutDates(), BookingRespDto::setBlockoutDates);
        });
		return mapper;
	}
	
	
	//configure spring security supplied password encoder bean
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
