/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.Role;

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
