/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Reset;
import com.visionous.dms.pojo.Verification;

/**
 * @author delimeta
 *
 */
@Repository
public interface ResetRepository extends JpaRepository<Reset, Long>{

	/**
	 * @param token
	 * @return
	 */
	Reset findByToken(String token);

}
