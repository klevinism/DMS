/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Personnel;

/**
 * @author delimeta
 *
 */
public interface IPersonnelService {
	Personnel create(Personnel personnel) throws EmailExistsException, UsernameExistsException;
	
	Optional<Personnel> findById(Long id);
	
	Personnel update(Personnel oldPersonnel, Personnel newPersonnel) throws EmailExistsException, UsernameExistsException;

	/**
	 * @param roleName
	 * @return
	 */
	List<Personnel> findAllByRoleName(String roleName);

	/**
	 * @param id
	 */
	void deleteById(Long id);
	
	/**
	 * @param personnel
	 */
	void delete(Personnel personnel);

	/**
	 * @param personnel
	 * @return
	 */
	Personnel disable(Personnel personnel);

	/**
	 * @param id
	 * @return
	 */
	Personnel disableById(Long id);

	/**
	 * @param enabled
	 * @param active
	 * @param role_name
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(boolean enabled, boolean active,
			String role_name);

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
	List<Personnel> findAllByRoleNameAndAccount_Business_Id(String roleName, long businessId);

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
	 * @param currentBusinessId
	 * @return
	 */
	Optional<Personnel> findByIdAndAccount_Businesses_Id(Long personnelId, Long currentBusinessId);

	/**
	 * @param enabled
	 * @param roleName
	 * @param businessId
	 * @return
	 */
	List<Personnel> findAllByAccount_EnabledAndAccount_Roles_NameAndAccount_Businesses_Id(boolean enabled,
			String roleName, long businessId);
}
