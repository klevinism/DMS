/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
@Repository
public interface HistoryRepository extends CrudRepository<History, Long>{

	/**
	 * @param supervisorId
	 * @return Optional {@link History} object
	 */
	Optional<History> findBySupervisorId(Long supervisorId);
	
	/**
	 * @param customerId
	 * @return Optional {@link History} object
	 */
	Optional<History> findByCustomerId(Long customerId);
}
