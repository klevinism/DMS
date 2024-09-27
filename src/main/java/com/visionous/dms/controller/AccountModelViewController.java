/**
 * 
 */
package com.visionous.dms.controller;

import jakarta.validation.Valid;

import com.o2dent.lib.accounts.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.AccountModelController;

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
	public String accountPost(@Valid Account account, BindingResult bindingResult, @RequestParam(name = "profileimage", required =false) MultipartFile profileImage,
							  @RequestParam(required = false) String action,
							  Model model) {
		
		accountModelController.init()
			.addControllerParam("profileimage", profileImage)
			.addControllerParam("action", action)
			.addModelAttributes(account)
			.addBindingResult(bindingResult)
			.addControllerParam("viewType", Actions.VIEW) 
			.setViewModel(model)
			.run(); // GetValuesForView
		
		if(bindingResult.hasErrors()) {
			return "demo_1/pages/"+action.toLowerCase()+"_account";
		}
		
		if(!account.getId().equals(AccountUtil.currentLoggedInUser().getAccount().getId())) {
			if(!AccountUtil.currentLoggedInUser().isPersonnel()) {
				return "redirect:/customer/dashboard/"+account.getId();
			}else if(AccountUtil.currentLoggedInUser().isPersonnel()) {
				return "redirect:/admin/personnel/dashboard/"+account.getId();
			}else {
				return "/";
			}
		}else {
			return "redirect:/account";
		}
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String accountProfile(Model model) {
		
		accountModelController.init()
			.addControllerParam("action", Actions.VIEW)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/view_account";
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
