package com.visionous.dms.service;


import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.lib.accounts.helpers.exceptions.SubdomainExistsException;

import java.util.Optional;

public interface IBusinessService {
	
	Business update(Business loggedInBusiness);
	
	Business create(Business loggedInBusiness) throws SubdomainExistsException;
	
	Business disable(Business business);
	
	Optional<Business> findById(Long businessId);
}
