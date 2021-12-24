package com.visionous.dms.service;

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

	public Business update(Business loggedInBusiness) {
		return this.businessRepository.saveAndFlush(loggedInBusiness);
	}
	
}
