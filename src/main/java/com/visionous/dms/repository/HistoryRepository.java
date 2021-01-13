/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{

	/**
	 * @param supervisorId
	 * @return Optional {@link History} object
	 */
	Set<History> findBySupervisorId(Long supervisorId);
	
	/**
	 * @param customerId
	 * @return Optional {@link History} object
	 */
	Optional<History> findByCustomerId(Long customerId);
}
