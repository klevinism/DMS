package com.visionous.dms.controller;


import javax.validation.Valid;

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
import com.visionous.dms.model.PersonnelModelController;
import com.visionous.dms.pojo.Personnel;

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
	@GetMapping("/{id}")
	public String personnelEdit(@PathVariable("id") Long id,
			@RequestParam(name="view", required=false) Actions view,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {

		personnelModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", view)
			.addControllerParam("viewType", view)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/personnel";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String personnelDefault(Model model) {
		personnelModelController.init()
		.addControllerParam("viewType", Actions.VIEW.getValue())
		.setViewModel(model)
		.run(); // GetValuesForView
		
		return "demo_1/pages/personnel"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("") 
	public String personnelPost(@Valid Personnel personnel, BindingResult errors, @RequestParam(name = "profileimage", required =false) MultipartFile profileImage,
			@RequestParam(required = false) String action,
			Model model) {
		
		personnelModelController.init()
			.addControllerParam("profileimage", profileImage)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.VIEW)
			.addBindingResult(errors)
			.addModelAttributes(personnel)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		if(personnelModelController.hasResultBindingError()) {
			return "demo_1/pages/"+action.toLowerCase()+"_personnel";
		}
		
		return "demo_1/pages/personnel"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String personnelView(@Valid @PathVariable("id") Long id, Model model) {
		
		personnelModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/view_personnel";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String personnelEdit(@Valid @PathVariable("id") Long id, Model model) {
		
		personnelModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.EDIT.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_personnel";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String personnelDelete(@Valid @PathVariable("id") Long id, Model model) {
		
		personnelModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.DELETE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_personnel";
	}
		
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String personnelCreate(Model model) {
		
		personnelModelController.init()
			.addControllerParam("viewType", Actions.CREATE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/create_personnel";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/dashboard/{id}")
	public String personnelDashboard(@Valid @PathVariable("id") Long id, Model model) {

		personnelModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/dashboard_personnel";
	}
	
}
