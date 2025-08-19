package com.app.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.custom_exceptions.ApiException;
import com.app.custom_exceptions.FileUploadException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.PropertyDao;
import com.app.dao.PropertyImageDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.dto.PropertyImageReqDto;
import com.app.dto.PropertyImageRespDto;
import com.app.entities.Property;
import com.app.entities.PropertyImage;
import com.app.entities.User;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PropertyImageServiceImpl implements PropertyImageService {

    private final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");
    private final UserDao userDao;
    private final PropertyDao propertyDao;
    private final ModelMapper modelMapper;
    private final PropertyImageDao propertyImageDao;

    private MultipartFile validateDto(PropertyImageReqDto dto) {
        MultipartFile file = dto.getFile();
        if (dto == null || file == null || file.isEmpty()) {
            throw new FileUploadException("Image file is required and should not be blank");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new FileUploadException("Only JPEG, PNG images are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5 MB limit
            throw new FileUploadException("File size must not exceed 5 MB");
        }
        return file;
    }

    @Override
    public ApiResponse addPropertyImages(Long propertyId, PropertyImageReqDto dto) throws IOException {
        MultipartFile file = validateDto(dto);
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid property id"));
        if (!property.getOwner().getEmail().equals(email)) {
            throw new ApiException("You are not authorized to add property images to this property");
        }
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setFileName(file.getOriginalFilename());
        propertyImage.setImage(file.getBytes());
        propertyImage.setProperty(property);
        PropertyImage savedImage = propertyImageDao.save(propertyImage);
        return new ApiResponse("Image added successfully with ID: " + savedImage.getId());
    }

    @Override
    public ApiResponse removePropertyImages(Long propertyImageId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        PropertyImage propertyImage = propertyImageDao.findById(propertyImageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        if (!propertyImage.getProperty().getOwner().getEmail().equals(email)) {
            throw new ApiException("You are not authorized to delete property images to this property");
        }
        propertyImage.setImageStatus(false);
        return new ApiResponse("Property image removed with id " + propertyImageId);
    }

    @Override
    public ApiResponse updatePropertyImage(Long propertyImageId, PropertyImageReqDto dto) throws IOException {
        MultipartFile file = validateDto(dto);
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        PropertyImage propertyImage = propertyImageDao.findById(propertyImageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        if (!propertyImage.getProperty().getOwner().getEmail().equals(email)) {
            throw new ApiException("You are not authorized to update property images to this property");
        }
        propertyImage.setFileName(file.getOriginalFilename());
        propertyImage.setImage(file.getBytes());
        PropertyImage updatedImage = propertyImageDao.save(propertyImage);
        return new ApiResponse("Image updated successfully with id " + updatedImage.getId());
    }

    @Override
    public List<PropertyImageRespDto> getPropertyImages(Long propertyId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User owner = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid property id"));
        boolean isOwner = property.getOwner().getEmail().equals(email);
        boolean isCustomer = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

        if (!isOwner && !isCustomer) {
            throw new ApiException("You are not authorized to view property images for this property");
        }
        List<PropertyImage> images = propertyImageDao.findByPropertyIdAndImageStatusTrue(propertyId);
        return images.stream()
                .map(image -> new PropertyImageRespDto(
                        image.getId(),
                        "/properties/" + propertyId + "/propertyImages/" + image.getId(), // Return image data URL
                        image.getProperty().getId(),
                        "Image retrieved successfully",
                        true))
                .collect(Collectors.toList());
    }

    @Override
    public PropertyImage getPropertyImageData(Long propertyId, Long imageId) {
        Property property = propertyDao.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid property id"));
        PropertyImage image = propertyImageDao.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with ID: " + imageId));
        if (!image.getProperty().getId().equals(propertyId) || !image.isImageStatus()) {
            throw new ResourceNotFoundException("Image not found or not associated with property");
        }
        return image;
    }
}