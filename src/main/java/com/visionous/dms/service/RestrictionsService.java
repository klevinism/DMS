/**
 * 
 */
package com.visionous.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.repository.RestrictionsRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RestrictionsService implements IRestrictionsService {

	private RestrictionsRepository restrictionsRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RestrictionsService(RestrictionsRepository restrictionsRepository) {
		this.restrictionsRepository = restrictionsRepository;
	}
}
