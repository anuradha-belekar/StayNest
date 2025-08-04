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
@Table(name = "images")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "property")
public class PropertyImage extends BaseEntity{
	@Column(name = "image_url",nullable = false)
	private String imageUrl;
	
	@ManyToOne
	@JoinColumn(name = "property_id",nullable = false)
	private Property property;
	
	
	
	public PropertyImage(String imageUrl, Property property) {
		super();
		this.imageUrl = imageUrl;
		this.property = property;
	}
	
	
	
	
	
	
}
