/**
 * 
 */
package com.visionaus.dms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;

/**
 * @author delimeta
 *
 */
public class ModelController implements ModelControllerImpl{
	
	private Map<String, Object> paramMap;
	private Model viewModel;
	
	/**
	 * @param model
	 */
	public ModelController(Model model) {
		this.viewModel = model;
		paramMap = new HashMap<>();
	}
	
	/**
	 * Default Constructor
	 */
	public ModelController() {
		paramMap = new HashMap<>();
	}
	
	
	/**
	 * @param key String
	 * @param value Object
	 * @return this ModelController
	 */
	protected ModelController addModelCollectionToView(String key, Object value) {
		this.viewModel.addAttribute(key,value);
		return this;
	}
	
	/**
	 * 
	 */
	public ModelController setViewModel(Model model) {
		this.viewModel = model;
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
	 * @return this ModelControllerImpl
	 */
	@Override
	public ModelController addControllerParam(String key, Object value) {
		paramMap.put(key, value);
		return this;
	}

	/**
	 * @return this ModelControllerImpl
	 */
	@Override
	public ModelControllerImpl removeModelParam(String key) {
		paramMap.remove(key);
		return this;
	}

	/**
	 * @return this ModelControllerImpl
	 */
	@Override
	public ModelControllerImpl setModelParam(String key, Object newValue) {
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
	
}
