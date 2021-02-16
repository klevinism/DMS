/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.Teeth;

/**
 * @author delimeta
 *
 */
public interface ITeethService {

	/**
	 * @param name
	 * @return
	 */
	Optional<Teeth> findByName(String name);

	/**
	 * @return
	 */
	List<Teeth> findAll();

}
