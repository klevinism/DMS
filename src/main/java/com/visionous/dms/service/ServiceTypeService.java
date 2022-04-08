/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.repository.ServiceTypeRepository;

/**
 * @author delimeta
 *
 */
@Service
public class ServiceTypeService implements IServiceTypeService{

	private ServiceTypeRepository serviceTypeRepository;
	
	/**
	 * 
	 */
	@Autowired
	public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
		this.serviceTypeRepository = serviceTypeRepository;
	}
	
	/**
	 * @param name
	 * @return
	 */
	@Override
	public Optional<ServiceType> findByName(String name) {
		return this.serviceTypeRepository.findByName(name);
	}

	/**
	 * @return
	 */
	@Override
	public List<ServiceType> findAll() {
		return this.serviceTypeRepository.findAll();
	}

	/**
	 * @param service
	 * @return
	 */
	@Override
	public ServiceType create(ServiceType service) {
		return this.serviceTypeRepository.saveAndFlush(service);
	}

	/**
	 * @param currentServiceId
	 * @return
	 */
	@Override
	public Optional<ServiceType> findById(Long serviceId) {
		return this.serviceTypeRepository.findById(serviceId);
	}

	/**
	 * @param serviceType
	 * @return
	 */
	@Override
	public ServiceType update(ServiceType serviceType) {
		return create(serviceType);
	}

	/**
	 * @param serviceType
	 */
	@Override
	public void delete(ServiceType serviceType) {
		this.serviceTypeRepository.delete(serviceType);
	}

	/**
	 * @param id
	 * @return
	 */
	@Override
	public List<ServiceType> findAllByGlobalSettingsId(Long id) {
		return this.serviceTypeRepository.findAllByGlobalSettingsId(id);
	}

	/**
	 * @param selectedServiceId
	 * @param globalSettingsId
	 * @return
	 */
	@Override
	public Optional<ServiceType> findByIdAndGlobalSettingsId(Long selectedServiceId, Long globalSettingsId) {
		return this.serviceTypeRepository.findByIdAndGlobalSettingsId(selectedServiceId, globalSettingsId);
	}
	
}
