/**
 * 
 */
package com.visionous.dms.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

/**
 * @author delimeta
 *
 */
public class ModelControllerImpl implements ModelController{
	
	/**
	 * 
	 */
	private Map<String, Object> paramMap;
	private Model viewModel;
	
	private BindingResult resultBindingError;
	
	/**
	 * @param model
	 */
	public ModelControllerImpl(Model model) {
		this.viewModel = model;
		paramMap = new HashMap<>();
	}
	
	/**
	 * Default Constructor
	 */
	public ModelControllerImpl() {
		paramMap = new HashMap<>();
	}
	
	
	/**
	 * @param key String
	 * @param value Object
	 * @return this ModelController
	 */
	protected ModelControllerImpl addModelCollectionToView(String key, Object value) {
		this.viewModel.addAttribute(key,value);
		return this;
	}
	
	public Object getModelCollectionToView(String key) {
		return this.viewModel.getAttribute(key);
	}
	
	/**
	 * 
	 */
	public ModelControllerImpl setViewModel(Model model) {
		this.viewModel = model;
		return this;
	}
	
	/**
	 * Clears the model parameters of the current {@link ModelControllerImpl}. 
	 * @return this ModelController
	 */
	public ModelControllerImpl init() {
		this.paramMap.clear();
		return this;
	}

	/**
	 * 
	 */
	public void run() {
		/*
		 * Method will be overridden by subclasses
		 * For quick example ONLY, uncomment the line below and a test parameter will be
		 * added to the html page shown.
		 * 
		 * addCollectionToView("testParameter", "Hello World");
		 */
	}
	
	/**
	 * @return this ModelController
	 */
	@Override
	public ModelControllerImpl addControllerParam(String key, Object value) {
		if(value != null && !value.equals("")
				&& key != null && !key.equals("")) paramMap.put(key, value) ;
		return this;
	}

	/**
	 * @return this ModelController
	 */
	@Override
	public ModelControllerImpl removeControllerParam(String key) {
		if(key != null && !key.equals("")) 
			paramMap.remove(key);
		return this;
	}

	/**
	 * Replaces an already present model parameter value with a newValue
	 * @param key The String parameter key
	 * @param newValue The new value of the parameter to replace
	 * @return this {@link ModelControllerImpl} object
	 */
	@Override
	public ModelControllerImpl setControllerParam(String key, Object newValue) {
		if(newValue != null && !newValue.equals("")
				&& key != null && !key.equals("")) 
			paramMap.replace(key, paramMap.get(key), newValue);
		return this;
	}
	
	/**
	 * @return paramMap Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAllControllerParams(){
		return this.paramMap;
	}
	
	/**
	 * Add model to collection.
	 */
	@Override
	public <T> ModelController addModelAttributes(T object) {
		addModelCollectionToView(object.getClass().getSimpleName(), object);
		return this;
	}

	@Override
	public ModelController addBindingResult(BindingResult bindingResult) {
		this.resultBindingError = bindingResult;
		return this;
	}

	/**
	 * @return the resultBindingError
	 */
	public boolean hasResultBindingError() {
		if(resultBindingError == null) return false;
		return resultBindingError.hasErrors();
	}
	
	public BindingResult getBindingResult() {
		return resultBindingError;
	}
}
