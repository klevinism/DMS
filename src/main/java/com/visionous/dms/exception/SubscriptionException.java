/**
 * 
 */
package com.visionous.dms.exception;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
public class SubscriptionException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	/**
	 * 
	 */
	public SubscriptionException() {
	}
	
	/**
	 * @param message
	 */
	public SubscriptionException(String message) {
		super(message);
	}
}