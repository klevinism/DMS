package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.exception.SubdomainExistsException;
import com.visionous.dms.pojo.Business;
import com.visionous.dms.repository.BusinessRepository;

@Service
public class BusinessService implements IBusinessService{
	private BusinessRepository businessRepository;
	
	@Autowired
	public BusinessService(BusinessRepository businessRepository) {
		this.businessRepository = businessRepository;
	}

	@Override
	public Business update(Business business) {
		return this.businessRepository.saveAndFlush(business);
	}

	@Override
	public Business create(Business business) throws SubdomainExistsException {
		Optional<List<Business>> businessFound = this.businessRepository.findBySubdomainUri(business.getSubdomainUri());
		if(businessFound.isPresent() 
				&& !businessFound.get().isEmpty()) {
			throw new SubdomainExistsException();
		}
		
		return this.businessRepository.saveAndFlush(business);
	}
	
	@Override
	public Business disable(Business business) {
		business.setEnabled(false);
		return this.businessRepository.saveAndFlush(business);
	}
	
	@Override
	public Optional<Business> findById(Long businessId) {
		return this.businessRepository.findById(businessId);
	}
}
