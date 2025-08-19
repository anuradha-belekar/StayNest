package com.app.service;

import java.io.IOException;
import java.util.List;

import com.app.dto.ApiResponse;
import com.app.dto.PropertyImageReqDto;
import com.app.dto.PropertyImageRespDto;
import com.app.entities.PropertyImage;

public interface PropertyImageService {
    ApiResponse addPropertyImages(Long propertyId, PropertyImageReqDto dto) throws IOException;
    ApiResponse removePropertyImages(Long propertyImageId);
    ApiResponse updatePropertyImage(Long propertyImageId, PropertyImageReqDto dto) throws IOException;
    List<PropertyImageRespDto> getPropertyImages(Long propertyId);
    PropertyImage getPropertyImageData(Long propertyId, Long imageId); // New method
}