/**
 * 
 */
package com.visionous.dms.controller;

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
import com.visionous.dms.model.RecordModelController;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Record;

/**
 * @author delimeta
 *
 */

@Controller
@RequestMapping("/admin/customer/{customerId}/history/{historyId}/record")
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
	@PostMapping("") 
	public String recordPost(@ModelAttribute Record record,
			@PathVariable("customerId") Long customerId,
			@PathVariable("historyId") Long historyId,
			@RequestParam(required = false) String action,
			Model model) {
		
		recordModelController.init()
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("action", action)
			.addControllerParam("viewType", Actions.VIEW)
			.addModelAttributes(record)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/record"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String recordDefault(@PathVariable("customerId") Long customerId,
			@PathVariable("historyId") Long historyId,
			@RequestParam(name="action", required=false) Actions action,
			@RequestParam(name="modal", required=false) boolean modal,
			Model model) {

		recordModelController.init() // Re-initialize Model
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("viewType", Actions.VIEW)
			.addControllerParam("modal", modal)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/record";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String recordCreate(@PathVariable("customerId") Long customerId,
			@PathVariable("historyId") Long historyId, Model model) {
		

		recordModelController.init() // Re-initialize Model
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("viewType", Actions.CREATE)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/mouth";
	}
}
