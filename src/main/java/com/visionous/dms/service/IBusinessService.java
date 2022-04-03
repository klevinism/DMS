package com.visionous.dms.service;

import java.util.Optional;

import com.visionous.dms.pojo.Business;

public interface IBusinessService {
	
	Business update(Business loggedInBusiness);
	
	Business create(Business loggedInBusiness);
	
	Business disable(Business business);
	
	Optional<Business> findById(Long businessId);
}
