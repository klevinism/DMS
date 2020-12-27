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


	private PersonnelController personnelMvc;
	
	/**
	 * 
	 */
	@Autowired
	public PersonnelModelViewController(PersonnelController personnelMvc) {
		this.personnelMvc = personnelMvc;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String personnel(Model model) {

		personnelMvc.setModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/personnel"; 
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/admin/personnel/edit/{id}")
	public String personnelModal(@PathVariable("id") Long id,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {
		
		System.out.println(modal);
		
		personnelMvc.setModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/personnel";
	}
}
