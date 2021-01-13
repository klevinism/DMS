/**
 * 
 */
package com.visionous.dms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.CustomerModelController;
import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/admin/customer")
public class CustomerModelViewController {

	private CustomerModelController customerModelController;
	
	/**
	 * 
	 */
	@Autowired
	public CustomerModelViewController(CustomerModelController customerModalController) {
		this.customerModelController = customerModalController;
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}")
	public String customerView(@PathVariable("id") Long id,
			@RequestParam(name="action", required=false) Actions action,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {

		customerModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", modal)
			.addControllerParam("viewType", Actions.VIEW.getValue())
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
		customerModelController.init()
		.addControllerParam("viewType", Actions.VIEW.getValue())
		.setViewModel(model)
		.run(); // GetValuesForView
		
		return "demo_1/pages/customer"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("")
	public String customerPost(@ModelAttribute Customer customer,
			@RequestParam(required = false) String[] rolez,
			@RequestParam(required = false) String action,
			Model model) {
		
		customerModelController.init()
			.addControllerParam("roles", rolez)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.VIEW)
			.addModelAttributes(customer)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/customer"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String customerEdit(@PathVariable("id") Long id, Model model) {
		
		customerModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.EDIT.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_customer";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String customerDelete(@Valid @PathVariable("id") Long id, Model model) {
		
		customerModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.DELETE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_customer";
	}
		
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String customerCreate(Model model) {
		
		customerModelController.init()
			.addControllerParam("viewType", Actions.CREATE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/create_customer";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/dashboard/{id}")
	public String customerDashboard(@Valid @PathVariable("id") Long id, Model model) {

		customerModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/dashboard_customer";
	}
}
