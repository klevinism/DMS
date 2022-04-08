package com.visionous.dms.exception;

import com.visionous.dms.configuration.helpers.DmsCore;

public class SubdomainExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	/**
	 * 
	 */
	public SubdomainExistsException() {
		
	}
	
	public SubdomainExistsException(String message) {
		super(message);
	}
}
