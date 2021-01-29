/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
