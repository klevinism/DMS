/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Teeth;

/**
 * @author delimeta
 *
 */
@Repository
public interface TeethRepository extends JpaRepository<Teeth, Long>{

	/**
	 * @param name
	 * @return
	 */
	Optional<Teeth> findByName(String name);
	
}
