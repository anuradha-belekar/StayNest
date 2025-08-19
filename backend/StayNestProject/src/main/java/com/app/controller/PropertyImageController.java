package com.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PropertyImageReqDto;
import com.app.dto.PropertyImageRespDto;
import com.app.entities.PropertyImage;
import com.app.service.PropertyImageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class PropertyImageController {
    private final PropertyImageService propertyImageService;

    @PostMapping(value = "/properties/{propertyId}/propertyImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(description = "add new property_images")
    public ResponseEntity<?> addPropertyImages(@PathVariable Long propertyId, @ModelAttribute PropertyImageReqDto dto) throws IOException {
        System.out.println("in add propertyImages " + propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(propertyImageService.addPropertyImages(propertyId, dto));
    }

    @DeleteMapping("/propertyImages/{propertyImageId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(description = "delete property_images")
    public ResponseEntity<?> deletePropertyImages(@PathVariable Long propertyImageId) {
        System.out.println("in delete propertyImages " + propertyImageId);
        return ResponseEntity.status(HttpStatus.OK).body(propertyImageService.removePropertyImages(propertyImageId));
    }

    @PutMapping(value = "/propertyImages/{propertyImageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(description = "update property_Images")
    public ResponseEntity<?> updatePropertyImages(@PathVariable Long propertyImageId, @ModelAttribute PropertyImageReqDto dto) throws IOException {
        System.out.println("in update propertyImage " + propertyImageId);
        return ResponseEntity.status(HttpStatus.OK).body(propertyImageService.updatePropertyImage(propertyImageId, dto));
    }

    @GetMapping("/properties/{propertyId}/propertyImages")
    @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER')")
    @Operation(description = "get all property Images metadata")
    public ResponseEntity<List<PropertyImageRespDto>> getPropertyImages(@PathVariable Long propertyId) {
        System.out.println("in get all property images " + propertyId);
        return ResponseEntity.status(HttpStatus.OK).body(propertyImageService.getPropertyImages(propertyId));
    }

    @GetMapping(value = "/properties/{propertyId}/propertyImages/{imageId}", produces = {MediaType.IMAGE_JPEG_VALUE, "image/jfif"})
    @Operation(description = "get property image data by ID")
    public ResponseEntity<byte[]> getPropertyImageData(@PathVariable Long propertyId, @PathVariable Long imageId) {
        System.out.println("in get property image data " + imageId + " for property " + propertyId);
        PropertyImage image = propertyImageService.getPropertyImageData(propertyId, imageId);
        String contentType = image.getFileName().endsWith(".jfif") ? "image/jfif" : MediaType.IMAGE_JPEG_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image.getImage());
    }
}