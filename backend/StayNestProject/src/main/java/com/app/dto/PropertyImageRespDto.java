package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PropertyImageRespDto {
	
	private Long id;
    private String fileName;
    private Long propertyId;
    private String message;
    private boolean isSuccess;
}
