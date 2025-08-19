	package com.app.service;
	
	import java.util.List;
	
	import com.app.dto.AmenityReqDto;
	import com.app.dto.AmenityRespDto;
	import com.app.dto.ApiResponse;
	
	public interface AmenityService {
	
		ApiResponse addAmenity(AmenityReqDto dto);
	
		ApiResponse deleteAmenity(Long amenityId);
	
		ApiResponse updateAmenity(Long amenityId, AmenityReqDto dto);
	
		List<AmenityRespDto> getAllActiveAmenities();
	
	}
