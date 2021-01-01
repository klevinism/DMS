/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.repository.CrudRepository;

import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
public interface RecordRepository extends CrudRepository<History, Long>{
	
}

