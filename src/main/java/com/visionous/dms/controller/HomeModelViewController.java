/**
 * 
 */
package com.visionous.dms.controller;

import java.util.Objects;
import java.util.Optional;

import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.repository.GlobalSettingsRepository;
import com.visionous.dms.service.GlobalSettingsService;
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
	private final HomeModelController homeModelController;
	private final GlobalSettingsService globalSettingsService;

	/**
	 *
	 * @param homeModelController
	 * @param globalSettingsService
	 */
	@Autowired
	public HomeModelViewController(HomeModelController homeModelController, GlobalSettingsService globalSettingsService) {
		this.homeModelController = homeModelController;
		this.globalSettingsService = globalSettingsService;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String homeDefault(Model model) {
		
		if(Objects.isNull(AccountUtil.currentLoggedInBussines())) {
			return "redirect:/business/create";
		}

		homeModelController.init()
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/index";
	}
}