/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Verification;
import com.visionous.dms.repository.VerificationRepository;

/**
 * @author delimeta
 *
 */
@Service
public class VerificationService implements IVerificationService{
	
	private VerificationRepository verificationRepository;
	
	/**
	 * 
	 */
	@Autowired
	public VerificationService(VerificationRepository verificationRepository) {
		this.verificationRepository = verificationRepository;
	}

	/**
	 * @param accountId
	 * @return
	 */
	@Override
	public List<Verification> findByAccount_id(Long accountId) {
		return this.verificationRepository.findByAccount_id(accountId);
	}

	/**
	 * @param verificationToken
	 */
	@Override
	public Verification create(Verification verification) {
		return this.verificationRepository.saveAndFlush(verification);
	}

	/**
	 * @param token
	 * @return
	 */
	@Override
	public Verification findByToken(String token) {
		return this.verificationRepository.findByToken(token);
	}

	/**
	 * @param id
	 * @param now
	 * @return
	 */
	@Override
	public Optional<Verification> findByAccount_idAndExpirationDateAfter(Long id, LocalDateTime now) {
		return this.verificationRepository.findByAccount_idAndExpiryDateAfter(id, now);
	}
	
	
}
