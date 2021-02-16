/**
 * 
 */
package com.visionous.dms.service;

import com.visionous.dms.pojo.Reset;

/**
 * @author delimeta
 *
 */
public interface IResetService {

	/**
	 * @param resetObject
	 * @return
	 */
	Reset create(Reset resetObject);

	/**
	 * @param token
	 * @return
	 */
	Reset findByToken(String token);

}
