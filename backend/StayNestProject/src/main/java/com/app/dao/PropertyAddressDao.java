		package com.app.dao;
		
		import java.util.Optional;
		
		import org.springframework.data.jpa.repository.JpaRepository;
		
		import com.app.entities.Property;
		import com.app.entities.PropertyAddress;
		
		public interface PropertyAddressDao extends JpaRepository<PropertyAddress,Long>{
		
			Optional<PropertyAddress> findByProperty(Property property);
		
		}
