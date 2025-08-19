package com.app.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImageReqDto{
	@Schema(type = "string",format = "binary",description = "Image file (JPEG or PNG, max 5MB)")
	private MultipartFile file;

}
