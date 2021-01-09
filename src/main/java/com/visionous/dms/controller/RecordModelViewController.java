/**
 * 
 */
package com.visionous.dms.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.RecordModelController;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/admin/customer/history/record")
public class RecordModelViewController {
	
	private RecordModelController recordModelController;
	
	
	/**
	 * 
	 */
	@Autowired
	public RecordModelViewController(RecordModelController recordModelController) {
		this.recordModelController = recordModelController;
	}


	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}")
	public String historyDefault(@PathVariable("id") Long id,
			@RequestParam(name="action", required=false) Actions action,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {
		
		recordModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", modal)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/history";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String historyDefault(Model model) {
		
		recordModelController.init() // Re-initialize Model
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/mouth";
	}
}
