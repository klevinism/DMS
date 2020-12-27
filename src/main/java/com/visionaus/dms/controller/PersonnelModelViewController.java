/**
 * 
 */
package com.visionaus.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/admin/personnel")
public class PersonnelModelViewController {
	
	private PersonnelModelController personnelModelController;
	
	/**
	 * 
	 */
	@Autowired
	public PersonnelModelViewController(PersonnelModelController personnelModelMvc) {
		this.personnelModelController = personnelModelMvc;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String personnelDefault(Model model) {

		personnelModelController.setViewModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/personnel"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String personnelIndex(Model model) {

		personnelModelController.setViewModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/personnel"; 
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String personnelModel(@PathVariable("id") Long id,
			@RequestParam(name="modal", required=false) String modal,
			Model model) {
		
		personnelModelController.addControllerParam("id",id)
			.addControllerParam("modal", modal)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/personnel";
	}
}
