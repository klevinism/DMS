/**
 * 
 */
package com.visionous.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.SubscriptionModelController;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/admin/subscription")
public class SubscriptionModelViewController {
	
	private SubscriptionModelController subscriptionModelController;	
	/**
	 * 
	 */
	@Autowired
	public SubscriptionModelViewController(SubscriptionModelController subscriptionModelController) {
		this.subscriptionModelController = subscriptionModelController;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String recordSelected(Model model) {

		subscriptionModelController.init() // Re-initialize Model

		.addControllerParam("action", Actions.VIEW)
		.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/subscription";
	}
	
}
