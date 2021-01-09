/**
 * 
 */
package com.visionous.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.CustomerModelController;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/admin/customer")
public class CustomerModelViewController {

	private CustomerModelController customerModalController;
	
	/**
	 * 
	 */
	@Autowired
	public CustomerModelViewController(CustomerModelController customerModalController) {
		this.customerModalController = customerModalController;
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}")
	public String customerEdit(@PathVariable("id") Long id,
			@RequestParam(name="action", required=true) Actions action,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {
		
		customerModalController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", modal)
			.addControllerParam("action", action)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/customer";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String customerDefault(Model model) {
		customerModalController.init().setViewModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/customer"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("")
	public String personnelPost(@RequestBody String username, Model model) {
		System.out.println(username);
		System.out.println("POST");
		customerModalController.setViewModel(model).run(); // GetValuesForView
		
		return "demo_1/pages/customer"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String personnelEdit(@PathVariable("id") Long id, Model model) {
		
		customerModalController.init()
			.addControllerParam("id",id)
			.addControllerParam("action", Actions.EDIT)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_customer";
	}
	
}
