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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.model.CustomerModelController;
import com.visionous.dms.model.HistoryModelController;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Questionnaire;

/**
 * @author delimeta
 *
 */
@Service
@RequestMapping("/customer/{id}/history")
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
	@PostMapping("") 
	public String historyPost(@ModelAttribute History history,
			@RequestParam(required = true) String action,
			Model model) {

		historyModelController.init()
			.addControllerParam("action", action)
			.addControllerParam("viewType", action)
			.addModelAttributes(history)
			.setViewModel(model)
			.run(); // GetValuesForView
		
		return "demo_1/pages/create_history"; 
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("")
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
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String historyCreate(@PathVariable("id") Long id, Model model) {
		
		historyModelController.init() // Re-initialize Model
			.addControllerParam("id", id)
			.addControllerParam("viewType", Actions.CREATE)
			.addControllerParam("action", Actions.CREATE) //Create new History immidiately in view
			.addModelAttributes(new History())
			.setViewModel(model)
			.run(); // GetValuesForView

		if(historyModelController.getModelCollectionToView("historyId") != null) {
			Long historyId = Long.valueOf(historyModelController.getModelCollectionToView("historyId").toString());
			String redirectUrl = "/customer/" + id + "/history/"+historyId+"/record/create";
			
			return "redirect:"+redirectUrl;
		}else {
			return "demo_1/pages/history";
		}
		
	}
}
