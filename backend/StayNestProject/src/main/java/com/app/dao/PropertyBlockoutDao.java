package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Property;
import com.app.entities.PropertyBlockout;

public interface PropertyBlockoutDao extends JpaRepository<PropertyBlockout, Long>{

	List<PropertyBlockout> findByProperty(Property property);

}
