/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.repository.CrudRepository;

import com.visionous.dms.pojo.Personnel;

/**
 * @author delimeta
 *
 */
public interface PersonnelRepository extends CrudRepository<Personnel, Long> {
	
}
