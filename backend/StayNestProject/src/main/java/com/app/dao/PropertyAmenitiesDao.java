package com.app.dao;

import com.app.entities.Property;
import com.app.entities.PropertyAmenities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyAmenitiesDao extends JpaRepository<PropertyAmenities, Long> {
    List<PropertyAmenities> findByPropertyAndStatusTrue(Property property);
}