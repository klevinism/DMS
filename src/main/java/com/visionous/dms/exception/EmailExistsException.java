/**
 * 
 */
package com.visionous.dms.exception;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
public class EmailExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	/**
	 * 
	 */
	public EmailExistsException() {
		
	}
	
	public EmailExistsException(String message) {
		super(message);
	}
}