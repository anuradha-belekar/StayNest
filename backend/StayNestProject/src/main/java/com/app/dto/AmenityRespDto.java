package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmenityRespDto extends BaseDto{
	 	private String amenityName;
	    private String amenityDescription;
	    private boolean isAvailable;
	
}
