package com.visionous.dms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String register(@Valid IaoAccount user, Model model) {
		
		System.out.println(user);
		
		return "demo_1/pages/samples/register";
	}
}
