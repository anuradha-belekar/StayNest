package com.app.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class BaseDto {
	@JsonProperty(access =Access.READ_ONLY)
	private Long id;
	@JsonProperty(access =Access.READ_ONLY)
	private LocalDate createdOn;
	@JsonProperty(access =Access.READ_ONLY)
	private LocalDate updatedOn;

}
