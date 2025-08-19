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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AmenityReqDto;
import com.app.dto.AmenityRespDto;
import com.app.service.AmenityService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class AmenityController {
	
	private final AmenityService amenityService;
	@PostMapping("/amenities")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "Add new amenity")
	public ResponseEntity<?> addAmenity(@RequestBody AmenityReqDto dto){
		System.out.println("in add amenity"+dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(amenityService.addAmenity(dto));
	}
	
	
	@DeleteMapping("{amenityId}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "soft delete amenity")
	public ResponseEntity<?> deleteAmenity(@PathVariable Long amenityId){
		System.out.println("in delete amenity"+amenityId);
		return ResponseEntity.status(HttpStatus.OK).body(amenityService.deleteAmenity(amenityId));
	}
	
	@PutMapping("{amenityId}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "update amenities")
	public ResponseEntity<?> updateAmenity(@PathVariable Long amenityId,@RequestBody AmenityReqDto dto){
		System.out.println("in update amenity"+amenityId);
		return ResponseEntity.status(HttpStatus.OK).body(amenityService.updateAmenity(amenityId,dto));
	}
	
	
	@GetMapping("/amenities")
	@PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @Operation(description = "Get all active amenities")
    public ResponseEntity<List<AmenityRespDto>> getAllAmenities() {
        return ResponseEntity.status(HttpStatus.OK).body(amenityService.getAllActiveAmenities());
    }
	
	
	
}
