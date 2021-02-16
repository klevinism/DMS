/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author delimeta
 *
 */
public class DmsCore {
	
	public static final long SERIAL_VERSION_UID = 01L;
	
	/**
	 * 
	 */
	private DmsCore() {
	}
	
	public static String pathToTemp() {
		return new FileSystemResource("").getFile().getAbsolutePath();
	}
	
	public static String appMainPath() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath();
	}
}
