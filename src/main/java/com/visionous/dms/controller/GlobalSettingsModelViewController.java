/**
 * 
 */
package com.visionous.dms.controller;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.GlobalSettingsModelController;
import com.visionous.dms.pojo.IaoBusiness_GlobalSettings;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	 *
	 * @param globalSettings
	 * @param errors
	 * @param globalImage
	 * @param action
	 * @param model
	 * @return
	 */
	@PostMapping("")
	public String globalSettingsEdit(@Valid @ModelAttribute IaoBusiness_GlobalSettings iaoBusinessGlobalSettings, BindingResult errors,
									 @RequestParam(name = "globalImage", required =false) MultipartFile globalImage,
									 @RequestParam(name = "action", required = true) String action, Model model) {
		
		globalSettingsModelController.init()
			.addModelAttributes(iaoBusinessGlobalSettings)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.EDIT.getValue())
			.addControllerParam("businessImage", globalImage)
			.addBindingResult(errors)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/global_settings";
	}
}
