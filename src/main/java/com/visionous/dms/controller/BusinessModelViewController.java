package com.visionous.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.BusinessModelController;

@Controller
@RequestMapping("/business")
public class BusinessModelViewController {
	
	private BusinessModelController businessModelController;
	
	@Autowired
	public BusinessModelViewController(BusinessModelController businessModelController) {
		this.businessModelController = businessModelController;
	}
	
	@GetMapping("")
	public String businessDefault(Model model){
		businessModelController.init()
		.addControllerParam("viewType", Actions.VIEW.getValue())
		.setViewModel(model)
		.run(); // GetValuesForView

		if(businessModelController.hasResultBindingError()) {
			return "redirect:/login";
		}
		
		
		if(AccountUtil.currentLoggedInUser() != null) {
			if(AccountUtil.currentLoggedInUser().getCurrentBusiness() != null) {
				return "redirect:/home";
			}else {
				return "redirect:/business/create";
			}
		}else{
			return "demo_1/pages/business";
		}
	}
	
	@GetMapping("/create")
	public String createBusiness(Model model) {
		
		businessModelController.init()
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView

		if(businessModelController.hasResultBindingError()) {
			return "redirect:/login";
		}
		
		return "demo_1/pages/create_business";
	}
	
	@GetMapping("/{practice}")
	public String businessSelect(Model model, @PathVariable(name = "practice", required = true) Long practice){
		businessModelController.init()
		.addControllerParam("practice", practice)
		.addControllerParam("viewType", Actions.VIEW.getValue())
		.setViewModel(model)
		.run(); // GetValuesForView

		if(businessModelController.hasResultBindingError()) {
			return "redirect:/login";
		}
		
		return "redirect:/business";
	}
	

}