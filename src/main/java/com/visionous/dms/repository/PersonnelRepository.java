/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Personnel;

/**
 * @author delimeta
 *
 */
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

	/**
	 * @param id
	 * @return Long
	 */
	void deleteById(Long id);

	/**
	 *
	 * @param ids
	 * @return
	 */
	List<Personnel> findByIdIn(List<Long> ids);
}
