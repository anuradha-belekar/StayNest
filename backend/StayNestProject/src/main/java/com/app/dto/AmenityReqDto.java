package com.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmenityReqDto extends BaseDto{
	@Schema(description = "Name of the amenity (e.g., Wi-Fi, Parking)", example = "Wi-Fi")
	private String amenityName;
    @Schema(description = "Description of the amenity", example = "High-speed internet access")
	private String amenityDescription;
    @Schema(description = "Availability status of the amenity", example = "true")
	private boolean isAvailable;
}
