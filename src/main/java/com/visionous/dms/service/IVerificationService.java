/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.List;
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
	List<Verification> findByAccount_id(Long accountId);

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

	/**
	 * @param id
	 * @param now
	 * @return
	 */
	Optional<Verification> findByAccount_idAndExpirationDateAfter(Long id, LocalDateTime now);

}
