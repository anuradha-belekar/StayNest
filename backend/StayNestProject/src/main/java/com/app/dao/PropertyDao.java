package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.dto.PropertyDto;
import com.app.entities.Property;
import com.app.entities.User;

public interface PropertyDao extends JpaRepository<Property, Long>{

	boolean existsByPropertyNameAndPropertyAddress_CityAndPropertyAddress_StateAndPropertyAddress_ZipCodeAndPropertyAddress_AdrLine1(
			String propertyName, String city, String state,String zipcode, String adrLine1);

	List<Property> findByStatusTrue();

	List<Property> findByOwner(User user);
	
	
	//method for case-insensitive partial matching by city
		@Query("SELECT p FROM Property p WHERE LOWER(p.propertyAddress.city) LIKE LOWER(CONCAT('%', :city, '%')) AND p.status = true")
		 List<Property> findByPropertyAddress_CityContainingIgnoreCaseAndStatusTrue(@Param("city") String city);

		

}
