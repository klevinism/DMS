package com.visionous.dms.repository;

import java.util.Optional;

import com.o2dent.lib.accounts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author delimeta
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * @param roleName 
	 * @return Optional<Account>
	 */
	Optional<Role> findByName(String roleName);
}
