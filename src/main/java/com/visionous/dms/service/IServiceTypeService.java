/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.ServiceType;

/**
 * @author delimeta
 *
 */
public interface IServiceTypeService {

	/**
	 * @param name
	 * @return
	 */
	Optional<ServiceType> findByName(String name);

	/**
	 * @return
	 */
	List<ServiceType> findAll();

	/**
	 * @param service
	 * @return
	 */
	ServiceType create(ServiceType service);

	/**
	 * @param serviceId
	 * @return
	 */
	Optional<ServiceType> findById(Long serviceId);

	/**
	 * @param serviceType
	 * @return
	 */
	ServiceType update(ServiceType serviceType);

	/**
	 * @param serviceType
	 */
	void delete(ServiceType serviceType);

	/**
	 * @param id
	 * @return
	 */
	List<ServiceType> findAllByGlobalSettingsId(Long id);

}
