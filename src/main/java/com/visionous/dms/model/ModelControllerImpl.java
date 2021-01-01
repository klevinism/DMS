/**
 * 
 */
package com.visionous.dms.model;

import java.util.Map;

/**
 * @author delimeta
 *
 */
public interface ModelControllerImpl {
	/**
	 * A custom implementation of model view mappings. 
	 */
	void run();
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelControllerImpl addControllerParam(String key, Object value);
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelControllerImpl removeControllerParam(String key);
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelControllerImpl setControllerParam(String key, Object newValue);


	/**
	 * @return HashMap<String, Object>
	 */
	Map<String, Object> getAllControllerParams();
}
