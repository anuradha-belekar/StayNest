package com.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "property_address")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "property")
public class PropertyAddress extends BaseEntity{
	@Column(name = "adr_line1",length = 100)
	private String adrLine1;
	@Column(name = "adr_line2",length = 100)
	private String adrLine2;
	@Column(length = 30)
	private String city;
	@Column(length = 30)
	private String state;
	@Column(length = 30)
	private String country;
	@Column(length = 10,name = "zip_code")
	private String zipCode;
	@Column(precision = 9)
	private double latitude;
	@Column(precision = 9)
	private double longitude;
	@Column(name = "status",nullable = false)
	private boolean status=true;
	@OneToOne
	@JoinColumn(name = "property_id",nullable = false,unique = true)
	private Property property;
	public PropertyAddress(String adrLine1, String adrLine2, String city, String state, String country, String zipCode,
			double latitude, double longitude, boolean status, Property property) {
		super();
		this.adrLine1 = adrLine1;
		this.adrLine2 = adrLine2;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.status = status;
		this.property = property;
	}
	
	
	
	
	
}
