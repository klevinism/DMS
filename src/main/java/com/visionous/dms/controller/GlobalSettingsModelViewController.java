/**
 * 
 */
package com.visionous.dms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.AccountModelController;
import com.visionous.dms.model.GlobalSettingsModelController;
import com.visionous.dms.pojo.GlobalSettings;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/admin/global")
public class GlobalSettingsModelViewController {

	private GlobalSettingsModelController globalSettingsModelController;
	
	/**
	 * 
	 */
	@Autowired
	public GlobalSettingsModelViewController(GlobalSettingsModelController globalSettingsModelController) {
		this.globalSettingsModelController = globalSettingsModelController;
	}
	

	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String globalSettingDefault(Model model) {
		globalSettingsModelController.init()
		.addControllerParam("viewType", Actions.EDIT.getValue())
		.setViewModel(model)
		.run(); // GetValuesForView

		return "demo_1/pages/global_settings"; 
	}
	
	/**
	 * @param valid GlobalSettings settings
	 * @param BuildingResult errors
	 * @param String action
	 * @param model
	 * @return
	 */
	@PostMapping("")
	public String globalSettingsEdit(@Valid @ModelAttribute GlobalSettings globalSettings, BindingResult errors,
			@RequestParam(name = "globalImage", required =false) MultipartFile globalImage,			
			@RequestParam(name = "action", required = true) String action,  Model model) {
		
		globalSettingsModelController.init()
			.addModelAttributes(globalSettings)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.EDIT.getValue())
			.addControllerParam("businessImage", globalImage)
			.addBindingResult(errors)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/global_settings";
	}
}
