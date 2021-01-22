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
import com.visionous.dms.model.AccountModelController;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/account")
public class AccountModelViewController {
	
	private AccountModelController accountModelController;
	
	/**
	 * 
	 */
	@Autowired
	public AccountModelViewController(AccountModelController accountModelController) {
		this.accountModelController = accountModelController;
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("") 
	public String accountPost(@ModelAttribute Account account, @RequestParam Long[] rolez,
			@RequestParam(required = false) String action,
			Model model) {
		
		accountModelController.init() 
			.addControllerParam("action", action)
			.addControllerParam("roles", rolez)
			.addModelAttributes(account)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView

		if(account.getCustomer() != null) {
			return "redirect:/customer/dashboard/"+account.getId();
		}else if(account.getPersonnel() != null) {
			return "redirect:/admin/personnel/dashboard/"+account.getId();
		}else {
			return "/";
		}
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String accountEdit(@Valid @PathVariable("id") Long id, Model model) {
		
		accountModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("action", Actions.EDIT)
			.addControllerParam("viewType", Actions.EDIT)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/edit_account";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String accountView(@Valid @PathVariable("id") Long id, Model model) {
		
		accountModelController.init()
			.addControllerParam("id",id)
			.addControllerParam("action", Actions.VIEW)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/view_account";
	}
}
