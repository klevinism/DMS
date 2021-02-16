/**
 * 
 */
package com.visionous.dms.service;

import java.util.Optional;

import com.visionous.dms.pojo.Verification;

/**
 * @author delimeta
 *
 */
public interface IVerificationService {

	/**
	 * @param accountId
	 * @return
	 */
	Optional<Verification> findByAccount_id(Long accountId);

	/**
	 * @param verification
	 * @return
	 */
	Verification create(Verification verification);

	/**
	 * @param token
	 * @return
	 */
	Verification findByToken(String token);

}
