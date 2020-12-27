/**
 * 
 */
package com.visionaus.dms.controller;

import org.springframework.ui.Model;

/**
 * @author delimeta
 *
 */
public class ModelController {
	
	private Model model;
	
	/**
	 * @param model
	 */
	public ModelController(Model model) {
		this.model = model;
	}
	
	/**
	 * Default Constructor
	 */
	public ModelController() {
	}
	
	
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	protected ModelController addCollectionToView(String key, Object value) {
		this.model.addAttribute(key,value);
		return this;
	}
	
	/**
	 * 
	 */
	public ModelController setModel(Model model) {
		this.model = model;
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
}
