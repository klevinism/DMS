/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;

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
	 * @param b
	 * @param c
	 * @param string
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(boolean b, boolean c, String roles_name);

	/**
	 * @param string
	 * @return
	 */
	Iterable<Personnel> findAllByAccount_Roles_Name(String roles_name);
	
}
