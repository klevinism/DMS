/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Record;

/**
 * @author delimeta
 *
 */
@Repository
public interface RecordRepository extends CrudRepository<Record, Long>{
	
	/**
	 * @param historyId
	 * @return Set of {@link Record} object
	 */
	Set<Record> findByHistoryId(Long historyId);

}

