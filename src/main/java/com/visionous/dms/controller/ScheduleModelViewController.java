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
import com.visionous.dms.model.ScheduleModelController;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleModelViewController {
	
	private ScheduleModelController agendaModelController;
	
	
	/**
	 * 
	 */
	@Autowired
	public ScheduleModelViewController(ScheduleModelController agendaModelController) {
		this.agendaModelController = agendaModelController;
	}
	
	

	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String agendaDefault(Model model) {
		
		agendaModelController.init()
			.addControllerParam("action", Actions.VIEW)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/schedule";
	}
}
