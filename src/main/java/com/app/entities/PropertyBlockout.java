package com.app.entities;

import java.time.LocalDate;

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
@Table(name = "property_blockouts")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "property")
public class PropertyBlockout extends BaseEntity{
	@Column(name = "blockout_date",nullable = false)
	private LocalDate BlockOutDate;
	
	@ManyToOne
	@JoinColumn(name = "property_id",nullable = false)
	private Property property;
	
	
	public PropertyBlockout(LocalDate blockOutDate, Property property) {
		super();
		BlockOutDate = blockOutDate;
		this.property = property;
	}
	
	
	
	
	
}
