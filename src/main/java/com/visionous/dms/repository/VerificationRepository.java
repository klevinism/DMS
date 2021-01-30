/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Verification;

/**
 * @author delimeta
 *
 */
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>{

	/**
	 * @return
	 */
	Verification findByToken(String token);

	/**
	 * @param account
	 * @return
	 */
	Optional<Verification> findByAccount(Account account);

	/**
	 * @param id
	 * @return
	 */
	Optional<Verification> findByAccount_id(Long id);

}
