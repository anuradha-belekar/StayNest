package com.app.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PropertyAddressDto {
	@NotBlank(message = "address line1 required")
	private String adrLine1;
	private String adrLine2;
	@NotBlank(message = "city required")
	private String city;
	@NotBlank(message = "state required")
	private String state;
	@NotBlank(message = "country required")
	private String country;
	@NotBlank(message = "zipcode required")
	private String zipCode;

}
