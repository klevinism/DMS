/**
 * 
 */
package com.visionous.dms.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.CustomerModelController;
import com.visionous.dms.model.HistoryModelController;

/**
 * @author delimeta
 *
 */
@Service
@RequestMapping("/admin/customer/history")
public class HistoryModelViewController {
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private HistoryModelController historyModelController;
	
	@Autowired
	private HistoryModelViewController(HistoryModelController historyModelController) {
		this.historyModelController = historyModelController;
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
		
		historyModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("modal", modal)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/history";
	}
	
	
}
