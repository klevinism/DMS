/**
 * 
 */
package com.visionous.dms.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.HomeModelController;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/home")
public class HomeModelViewController {
	
	private HomeModelController homeModelController;
	
	/**
	 * 
	 */
	@Autowired
	public HomeModelViewController(HomeModelController homeModelController) {
		this.homeModelController = homeModelController;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String homeDefault(Model model) {
		
		homeModelController.init()
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		if(Objects.isNull(AccountUtil.currentLoggedInBussines())) {
			return "redirect:/business";
		}
		
		return "demo_1/index";
	}
	
	
}