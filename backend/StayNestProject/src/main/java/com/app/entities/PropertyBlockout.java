package com.app.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//this are for property maintainance or repairs or owner's personal use or special events
@Entity
@Table(name = "property_blockouts")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "property")
public class PropertyBlockout extends BaseEntity{
	@Column(name = "blockout_date",nullable = false)
	private LocalDate BlockOutDate;
	
	@Column(name = "status",nullable = false)
	private boolean status=true;
	@ManyToOne
	@JoinColumn(name = "property_id",nullable = false)
	@JsonBackReference
	private Property property;
	
	
	@ManyToOne
	@JoinColumn(name="booking_id")
	private Booking booking;
	
	
	public PropertyBlockout(LocalDate blockOutDate, Property property) {
		super();
		BlockOutDate = blockOutDate;
		this.property = property;
	}


	public PropertyBlockout(LocalDate blockOutDate, Property property, Booking booking) {
		super();
		BlockOutDate = blockOutDate;
		this.property = property;
		this.booking = booking;
	}


	public PropertyBlockout(LocalDate blockOutDate, boolean status, Property property, Booking booking) {
		super();
		BlockOutDate = blockOutDate;
		this.status = status;
		this.property = property;
		this.booking = booking;
	}
	
	
	
	
}
