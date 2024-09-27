package com.visionous.dms.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.SignUpModelController;
import com.visionous.dms.pojo.IaoAccount;

@Controller
@RequestMapping("/register")
public class SignUpModelViewController {
	
	private SignUpModelController signUpModel;
	
	@Autowired
	public SignUpModelViewController(SignUpModelController signUpModel) {
		this.signUpModel = signUpModel;
	}
	
	/**
	 * @return
	 */
	@GetMapping("")
	public String register(Model model) {
		signUpModel.init()
			.addControllerParam("viewType", Actions.CREATE.getValue())
			.setViewModel(model)
			.run(); // GetValuesForView
		return "demo_1/pages/samples/register";
	}
	
	/**
	 * @param user
	 * @param model
	 * @return
	 */
	@PostMapping("")
	public String createAccount(@Valid @ModelAttribute("iaoAccount") IaoAccount iaoAccount, BindingResult bindingResult, Model model, @RequestParam(required = false) String action) {
		
		signUpModel.init()
			.addControllerParam("viewType", Actions.VIEW.getValue())
			.addControllerParam("action", action)
			.setViewModel(model)
			.addModelAttributes(iaoAccount)
			.addBindingResult(bindingResult)
			.run(); // GetValuesForView
			
		
		if(signUpModel.hasResultBindingError()) {
			signUpModel.getBindingResult().getAllErrors().forEach(x -> System.out.println(x.toString()));
			return "demo_1/pages/samples/register";
		}
		return "demo_1/pages/samples/register";
	}
}
