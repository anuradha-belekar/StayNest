package com.app.controller;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.app.dao.PropertyDao;
import com.app.dto.PropertyDto;
import com.app.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@Validated
public class PropertyController {

    private final PropertyDao propertyDao;
    private final ModelMapper modelMapper;
    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @Operation(description = "Add New Property")
    public ResponseEntity<?> addNewProperty(@RequestBody PropertyDto dto) {
        System.out.println("in add " + dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addNewProperty(dto));
    }

    @DeleteMapping("/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> deleteProperty(@PathVariable Long propertyId) {
        System.out.println("in delete " + propertyId);
        return ResponseEntity.ok(propertyService.deletePropertyDetails(propertyId));
    }

    @GetMapping
    public ResponseEntity<?> getAllProperties() {
        System.out.println("in getallproperties");
        List<PropertyDto> propertiesList = propertyService.getProperties();
        if (propertiesList.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(propertiesList);
    }

    @GetMapping("/{propertyId}")
    @Operation(description = "Get property details by id")
    public ResponseEntity<?> getPropertyDetails(@PathVariable @Min(1) @Max(100) Long propertyId) {
        System.out.println("in get details " + propertyId);
        return ResponseEntity.ok(propertyService.getPropertyDetails(propertyId));
    }

    @PutMapping("/{propertyId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(description = "Update Property details")
    public ResponseEntity<?> updateDetails(@PathVariable Long propertyId, @RequestBody PropertyDto dto) {
        System.out.println("in update " + propertyId + dto);
        return ResponseEntity.ok(propertyService.updateProperty(propertyId, dto));
    }
    
    
    @GetMapping("/search/city/{city}")
    @Operation(description = "Search properties by city with case-insensitive partial matching")
    public ResponseEntity<?> searchPropertiesByCity(@PathVariable String city){
    	System.out.println("in searchPropertiesByCity: \" + city");
    	
    		List<PropertyDto> propertiesList = propertyService.serachPropertiesByCity(city);
    		if(propertiesList.isEmpty()) {
    			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    		}
    		return ResponseEntity.ok(propertiesList);
    	
    }
}