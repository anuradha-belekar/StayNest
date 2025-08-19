package com.app.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.custom_exceptions.ResourceAlreadyExistsException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.AmenityDao;
import com.app.dao.PropertyAmenitiesDao;
import com.app.dao.PropertyDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.dto.PropertyDto;
import com.app.entities.Amenities;
import com.app.entities.Property;
import com.app.entities.PropertyAddress;
import com.app.entities.PropertyAmenities;
import com.app.entities.User;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDao propertyDao;
    private final ModelMapper modelMapper;
    private final UserDao userDao;
    private final AmenityDao amenityDao;
    private final PropertyAmenitiesDao propertyAmenitiesDao;

    @Override
    public ApiResponse addNewProperty(PropertyDto dto) {
        if (propertyDao.existsByPropertyNameAndPropertyAddress_CityAndPropertyAddress_StateAndPropertyAddress_ZipCodeAndPropertyAddress_AdrLine1(
                dto.getPropertyName(),
                dto.getPropertyAddress().getCity(),
                dto.getPropertyAddress().getState(),
                dto.getPropertyAddress().getZipCode(),
                dto.getPropertyAddress().getAdrLine1()
        )) {
            throw new ResourceAlreadyExistsException("Duplicate Property found with same name and address.");
        }

        // Map DTO to Entity
        Property property = modelMapper.map(dto, Property.class);

        // Set owner from authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user email"));
        property.setOwner(owner);

        // Maintain bidirectional link for address
        PropertyAddress address = property.getPropertyAddress();
        address.setProperty(property);

        // Save property
        Property saved = propertyDao.save(property);

        // Add amenities
        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            for (Long amenityId : dto.getAmenityIds()) {
                Amenities amenity = amenityDao.findById(amenityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));
                PropertyAmenities propertyAmenity = new PropertyAmenities(property, amenity);
                propertyAmenitiesDao.save(propertyAmenity);
                property.getPropertyAmenities().add(propertyAmenity);
            }
        }

        return new ApiResponse("Property added with ID = " + saved.getId());
    }

    @Override
    public ApiResponse deletePropertyDetails(Long propertyId) {
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Property Id"));
        // Soft delete property
        property.setStatus(false);
        // Soft delete associated amenities
        List<PropertyAmenities> amenities = propertyAmenitiesDao.findByPropertyAndStatusTrue(property);
        amenities.forEach(amenity -> amenity.setStatus(false));
        propertyAmenitiesDao.saveAll(amenities);
        return new ApiResponse("Soft deleted property details");
    }

    @Override
    public List<PropertyDto> getProperties() {
        return propertyDao.findByStatusTrue()
                .stream()
                .map(property -> {
                    PropertyDto dto = modelMapper.map(property, PropertyDto.class);
                    List<Long> amenityIds = propertyAmenitiesDao.findByPropertyAndStatusTrue(property)
                            .stream()
                            .map(pa -> pa.getAmenity().getId())
                            .toList();
                    dto.setAmenityIds(amenityIds);
                    return dto;
                })
                .toList();
    }

    @Override
    public PropertyDto getPropertyDetails(@Min(1) @Max(100) Long propertyId) {
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Property id"));
        PropertyDto dto = modelMapper.map(property, PropertyDto.class);
        List<Long> amenityIds = propertyAmenitiesDao.findByPropertyAndStatusTrue(property)
                .stream()
                .map(pa -> pa.getAmenity().getId())
                .toList();
        dto.setAmenityIds(amenityIds);
        return dto;
    }

    @Override
    public ApiResponse updateProperty(Long propertyId, PropertyDto dto) {
        if (propertyDao.existsByPropertyNameAndPropertyAddress_CityAndPropertyAddress_StateAndPropertyAddress_ZipCodeAndPropertyAddress_AdrLine1(
                dto.getPropertyName(),
                dto.getPropertyAddress().getCity(),
                dto.getPropertyAddress().getState(),
                dto.getPropertyAddress().getZipCode(),
                dto.getPropertyAddress().getAdrLine1()
        )) {
            throw new ResourceAlreadyExistsException("Duplicate Property found with same name and address.");
        }

        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found: update failed"));

        // Update property fields
        modelMapper.map(dto, property);
        PropertyAddress address = property.getPropertyAddress();
        modelMapper.map(dto.getPropertyAddress(), address);

        // Update amenities
        List<PropertyAmenities> existingAmenities = propertyAmenitiesDao.findByPropertyAndStatusTrue(property);
        existingAmenities.forEach(amenity -> amenity.setStatus(false));
        propertyAmenitiesDao.saveAll(existingAmenities);

        if (dto.getAmenityIds() != null && !dto.getAmenityIds().isEmpty()) {
            for (Long amenityId : dto.getAmenityIds()) {
                Amenities amenity = amenityDao.findById(amenityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));
                PropertyAmenities propertyAmenity = new PropertyAmenities(property, amenity);
                propertyAmenitiesDao.save(propertyAmenity);
                property.getPropertyAmenities().add(propertyAmenity);
            }
        }

        return new ApiResponse("Property details updated");
    }
    
    @Override
	public List<PropertyDto> serachPropertiesByCity(String city) {
		// TODO Auto-generated method stub
		if(city == null || city.trim().isEmpty())
		{
			throw new IllegalArgumentException("City cannot be null or empty");
		}
		
		List<Property> properties = propertyDao.findByPropertyAddress_CityContainingIgnoreCaseAndStatusTrue(city.trim());
		if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found in city : " + city);
        }
		
		return properties.stream()
				.map(property -> {
					PropertyDto dto = modelMapper.map(property, PropertyDto.class);
					List<Long> amenityIds = propertyAmenitiesDao.findByPropertyAndStatusTrue(property)
                            .stream()
                            .map(pa -> pa.getAmenity().getId())
                            .toList();
                    dto.setAmenityIds(amenityIds);
                    return dto;		
				})
				.toList();
	}
}