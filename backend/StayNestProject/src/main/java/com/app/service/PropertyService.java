package com.app.service;

import java.util.List;

import com.app.dto.ApiResponse;
import com.app.dto.PropertyDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public interface PropertyService {
	
	ApiResponse addNewProperty(PropertyDto dto);

	ApiResponse deletePropertyDetails(Long propertyId);

	List<PropertyDto> getProperties();

	PropertyDto getPropertyDetails(@Min(1) @Max(100) Long propertyId);

	ApiResponse updateProperty(Long propertyId, PropertyDto dto);
	
	
	List<PropertyDto> serachPropertiesByCity(String city);
	
	
	
}
