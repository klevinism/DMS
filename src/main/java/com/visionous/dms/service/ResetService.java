/**
 * 
 */
package com.visionous.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Reset;
import com.visionous.dms.repository.ResetRepository;

/**
 * @author delimeta
 *
 */
@Service
public class ResetService implements IResetService{
	private ResetRepository resetRepository;
	
	/**
	 * 
	 */
	@Autowired
	public ResetService(ResetRepository resetRepository) {
		this.resetRepository = resetRepository;
	}

	/**
	 * @param resetToken
	 */
	@Override
	public Reset create(Reset resetObject) {
		return this.resetRepository.saveAndFlush(resetObject);
	}

	/**
	 * @param token
	 * @return
	 */
	@Override
	public Reset findByToken(String token) {
		return this.resetRepository.findByToken(token);
	}
	
	
}
