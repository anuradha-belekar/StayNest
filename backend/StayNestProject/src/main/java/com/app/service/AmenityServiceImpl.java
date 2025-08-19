package com.app.service;

import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.custom_exceptions.InvalidInputException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.AmenityDao;
import com.app.dao.UserDao;
import com.app.dto.AmenityReqDto;
import com.app.dto.AmenityRespDto;
import com.app.dto.ApiResponse;
import com.app.entities.Amenities;
import com.app.entities.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AmenityServiceImpl implements AmenityService {
	
	private final UserDao userDao;
	private final ModelMapper modelmapper;
	private final AmenityDao amenityDao;
	
	private void validateDto(AmenityReqDto dto) {
		// TODO Auto-generated method stub
		if(dto==null || dto.getAmenityName()== null || dto.getAmenityName().isEmpty()) {
			throw new InvalidInputException("Amenity name is required and should not blank");
		}
		
	}
	
	private void validateAdmin() {
		//Authenticated user
				String email=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

				//validate User
				
				User admin=userDao.findByEmail(email).orElseThrow(()->new
				  ResourceNotFoundException("User not found with email:" + email));
				

			    if (!admin.getRole().name().equals("ROLE_ADMIN")) {
			        throw new InvalidInputException("Only admin can create new amenities");
			    }		
	}
	
	@Override
	public ApiResponse addAmenity(AmenityReqDto dto) {
		// TODO Auto-generated method stub
		validateDto(dto);
		
		validateAdmin();
		Amenities amenities=modelmapper.map(dto, Amenities.class);
		amenities.setAmenityStatus(true);
		Amenities savedAmenities=amenityDao.save(amenities);
		
		return new ApiResponse("Amenity added successfully with id"+savedAmenities.getId());
	}

	@Override
	public ApiResponse deleteAmenity(Long amenityId) {
	
        validateAdmin();
        Amenities amenity=amenityDao.findById(amenityId)
        		.orElseThrow(()->new ResourceNotFoundException("Amenity not found with id"+amenityId));
        if(!amenity.isAmenityStatus()) {
        	throw new InvalidInputException("Amenity is already deleted");
        }
        
        amenity.setAmenityStatus(false);
        amenityDao.save(amenity);
		return new ApiResponse("Amenity deleted successfully with id"+amenityId);
	}

	@Override
	public ApiResponse updateAmenity(Long amenityId, AmenityReqDto dto) {
		
		validateDto(dto);
		validateAdmin();
		Amenities amenity=amenityDao.findById(amenityId)
        		.orElseThrow(()->new ResourceNotFoundException("Amenity not found with id"+amenityId));
        
		 if(!amenity.isAmenityStatus()) {
	        	throw new InvalidInputException("Amenity is already deleted");
	        }
		 
		 modelmapper.map(dto, amenity);
		 amenityDao.save(amenity);
		return new ApiResponse("amenity updated successfully with id"+amenityId);
	}

	@Override
	public List<AmenityRespDto> getAllActiveAmenities() {
		// TODO Auto-generated method stub
		
		//validateAdmin();
		
		return amenityDao.findByAmenityStatusTrue()
				.stream()
				.map(amenity->modelmapper.map(amenity, AmenityRespDto.class))
				.collect(Collectors.toList());
	}



}
