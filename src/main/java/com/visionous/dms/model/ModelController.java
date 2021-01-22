/**
 * 
 */
package com.visionous.dms.model;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

/**
 * @author delimeta
 *
 */
public interface ModelController {
	/**
	 * A custom implementation of model view mappings. 
	 */
	void run();
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelController addControllerParam(String key, Object value);
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelController removeControllerParam(String key);
	
	/**
	 * @return ModelControllerImpl
	 */
	ModelController setControllerParam(String key, Object newValue);


	/**
	 * @return HashMap<String, Object>
	 */
	Map<String, Object> getAllControllerParams();


	/**
	 * @param <T>
	 * @param object
	 * @return
	 */
	<T> ModelController addModelAttributes(T object);

	/**
	 * @param model
	 * @return
	 */
	ModelController setViewModel(Model model);

	/**
	 * @param bindingResult
	 * @return
	 */
	ModelController addBindingResult(BindingResult bindingResult);
}
