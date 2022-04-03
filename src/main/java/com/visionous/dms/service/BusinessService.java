package com.visionous.dms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Business create(Business business) {
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
