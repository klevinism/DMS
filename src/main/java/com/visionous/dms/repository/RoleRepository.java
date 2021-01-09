/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Role;

/**
 * @author delimeta
 *
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	/**
	 * @param roleName 
	 * @return Optional<Account>
	 */
	Optional<Role> findByName(String roleName);
}
