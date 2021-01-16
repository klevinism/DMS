/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visionous.dms.pojo.ServiceType;

/**
 * @author delimeta
 *
 */
public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long>{


	/**
	 * @param name
	 * @return
	 */
	Optional<ServiceType> findByName(String name);

}
