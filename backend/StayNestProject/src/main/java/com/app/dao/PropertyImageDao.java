package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Property;
import com.app.entities.PropertyImage;

public interface PropertyImageDao extends JpaRepository<PropertyImage, Long> {

	List<PropertyImage> findByPropertyId(Long propertyId);


	List<PropertyImage> findByPropertyIdAndImageStatusTrue(Long propertyId);


	List<PropertyImage> findByProperty(Property property);

}
