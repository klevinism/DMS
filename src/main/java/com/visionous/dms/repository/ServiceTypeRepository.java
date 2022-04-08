/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visionous.dms.pojo.GlobalSettings;
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

	/**
	 * @param id
	 * @return
	 */
	List<ServiceType> findAllByGlobalSettingsId(Long id);

	
	/**
	 * @param selectedServiceId
	 * @param globalSettingsId
	 * @return
	 */
	Optional<ServiceType> findByIdAndGlobalSettingsId(Long selectedServiceId, Long globalSettingsId);

}
