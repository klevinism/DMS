/**
 * 
 */
package com.visionous.dms.exception;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
public class PhoneNumberExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	/**
	 * 
	 */
	public PhoneNumberExistsException() {
		
	}
	
	public PhoneNumberExistsException(String message) {
		super(message);
	}
}
