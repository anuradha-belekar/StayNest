package com.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "property_images")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "property")
public class PropertyImage extends BaseEntity{
	
	@Column(name = "file_name",nullable = false)
	private String fileName;
	
	@Lob
	@Column(name = "images",nullable = false, columnDefinition = "LONGBLOB")
	private byte[] image;
	
	 
	@ManyToOne
	@JoinColumn(name = "property_id",nullable = false)
	@JsonBackReference
	private Property property;

	
	@Column(name = "image_Status")
	private boolean imageStatus = true; //Added for soft delete


	public PropertyImage(String fileName, byte[] image, Property property, boolean imageStatus) {
		super();
		this.fileName = fileName;
		this.image = image;
		this.property = property;
		this.imageStatus = imageStatus;
	}

	
	
	
	
	
	
}
