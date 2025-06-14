/**
 * 
 */
package com.visionous.dms.controller;

import java.util.Date;

import com.o2dent.lib.accounts.entity.Account;
import com.visionous.dms.pojo.IaoAccount_Customer;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.CustomerModelController;
import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/customer")
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
	@GetMapping("/dashboard/{id}")
	public String customerDashboard(@PathVariable("id") Long id, Model model) {

		customerModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/dashboard_customer";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("{id}")
	public String singleCustomer(@PathVariable("id") Long id,
			@RequestParam(name="view", required=false) Actions action,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {

		customerModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", modal)
			.addControllerParam("action", action)
			.addControllerParam("viewType", (action!=null ? action : Actions.VIEW))
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
	public String customerPost(@Valid IaoAccount_Customer account, BindingResult errors,
							   @RequestParam(name = "profileimage", required =false) MultipartFile profileImage,
							   @RequestParam(required = false) String action,
							   Model model) {
		
		customerModelController.init()
			.addControllerParam("profileimage", profileImage)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.VIEW)
			.addBindingResult(errors)
			.addModelAttributes(account)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		if(customerModelController.hasResultBindingError()) {
			return "demo_1/pages/"+action.toLowerCase()+"_customer";
		}else if(model.getAttribute("errorEmail") != null || model.getAttribute("errorUsername") != null) {
			return "demo_1/pages/create_customer";
		}
		
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
	@GetMapping("/view/{id}") 
	public String customerView(@PathVariable("id") Long id, Model model) {
		
		customerModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/view_customer";
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
	public String customerCreate(@RequestParam(name = "errorEmail", required =false) String errorEmail, 
			@RequestParam(name = "errorUsername", required =false) String errorUsername, Model model) {
		
		if(errorEmail == null && errorUsername == null) {
			customerModelController.init()
				.addControllerParam("viewType", Actions.CREATE.getValue())
				.setViewModel(model)
				.run(); // GetValuesForView
		}
		return "demo_1/pages/create_customer";
	}
	
	
	@GetMapping("/book")
	public String customerBook(@PathVariable(name = "customerId", required = false) Long customerId,
			@PathVariable(name = "personnelId", required = false) Long personnelId,
			@PathVariable(name = "appointmentDate", required = false) Date appointmentDate, Model model) {

		customerModelController.init()
			.addControllerParam("viewType", Actions.CREATE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/create_customer";
	}
}
