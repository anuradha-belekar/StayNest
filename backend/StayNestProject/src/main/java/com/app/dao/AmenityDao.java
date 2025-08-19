package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.dto.AmenityRespDto;
import com.app.entities.Amenities;

public interface AmenityDao extends JpaRepository<Amenities, Long>{

	List<Amenities> findByAmenityStatusTrue();

}
