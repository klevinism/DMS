/**
 * 
 */
package com.visionous.dms.exception;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
public class UsernameExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	/**
	 * 
	 */
	public UsernameExistsException() {
		
	}
	
	public UsernameExistsException(String message) {
		super(message);
	}
}
