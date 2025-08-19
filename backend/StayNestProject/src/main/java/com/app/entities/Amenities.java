package com.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "amenities")
@NoArgsConstructor
@Getter
@Setter
@ToString()
public class Amenities extends BaseEntity{
	@Column(name = "amenity_name",length = 100,nullable = false)
	private String amenityName;
	@Column(name = "amenity_description",length = 200)
	private String amenityDescription;
	@Column(name = "is_amenity_available",nullable = false)
	private boolean isAvailable;
	@Column(name = "amenity_status", nullable = false)
    private boolean amenityStatus = true; // For soft delete
	public Amenities(String amenityName, String amenityDescription, boolean isAvailable, boolean amenityStatus) {
		super();
		this.amenityName = amenityName;
		this.amenityDescription = amenityDescription;
		this.isAvailable = isAvailable;
		this.amenityStatus = amenityStatus;
	}
	
	
	
	
	
}
