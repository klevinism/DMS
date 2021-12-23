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
	List<Personnel> findAllByAccount_Roles_Name(String roles_name);

	/**
	 * @param enabled
	 * @param role_name
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_Roles_Name(boolean enabled, String role_name);

	
	/**
	 * @param roleName
	 * @param businessId
	 * @return
	 */
	List<Personnel> findAllByAccount_Roles_NameAndAccount_Businesses_Id(String roleName, long businessId);

	/**
	 * @param enabled
	 * @param active
	 * @param roleName
	 * @param businessId
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_NameAndAccount_Businesses_Id(
			boolean enabled, boolean active, String roleName, long businessId);

	/**
	 * @param personnelId
	 * @param businessId
	 * @return
	 */
	Optional<Personnel> findByIdAndAccount_Businesses_Id(Long personnelId, Long businessId);

	/**
	 * @param enabled
	 * @param roleName
	 * @param businessId
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_Roles_NameAndAccount_Businesses_Id(boolean enabled,
			String roleName, long businessId);

	List<Personnel> findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_NameInAndAccount_Businesses_Id(boolean b,
			boolean c, List<String> roles, Long id);
	
}
