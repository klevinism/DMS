/**
 * 
 */
package com.visionous.dms.controller;

import java.util.List;

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

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.QuestionnaireModelController;
import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireResponse;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/customer/{id}/questionnaire")
public class QuestionnaireModelViewController {

	private QuestionnaireModelController questionnaireModelController;

	/**
	 * 
	 */
	@Autowired
	public QuestionnaireModelViewController(QuestionnaireModelController questionnaireModelController) {
		this.questionnaireModelController = questionnaireModelController;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String questionnaireEdit(@PathVariable("id") Long id, Model model) {
		questionnaireModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("viewType", Actions.CREATE)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/create_questionnaire";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("") 
	public String questionnairePost(@Valid Questionnaire questionnaire, BindingResult errors,
			@RequestParam(required = true) String action,
			Model model) {

		questionnaireModelController.init()
			.addControllerParam("action", action)
			.addControllerParam("viewType", action)
			.addBindingResult(errors)
			.addModelAttributes(questionnaire)
			.setViewModel(model)
			.run(); // GetValuesForView

		if(questionnaireModelController.hasResultBindingError()) {
			return "demo_1/pages/create_questionnaire";
		}
		
		String redirectUrl = "/customer/"+questionnaire.getCustomerId()+"/history/create";
		return "redirect:"+redirectUrl; 
	}
	 
}
