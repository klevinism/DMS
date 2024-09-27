/**
 * 
 */
package com.visionous.dms.service;

import com.o2dent.lib.accounts.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * @author delimeta
 *
 */
public interface IRoleService {

	/**
	 * @param name
	 * @return
	 */
	Optional<Role> findByName(String name);

	/**
	 * @return
	 */
	List<Role> findAll();

}
