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

import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.model.RecordReceiptModelController;
import com.visionous.dms.pojo.RecordReceipt;

/**
 * @author delimeta
 *
 */
@Controller
@RequestMapping("/customer/{customerId}/history/{historyId}/record/{recordId}/record_receipt")
public class RecordReceiptModelViewController {

	private RecordReceiptModelController recordReceiptModelController;
	
	/**
	 * 
	 */
	@Autowired
	public RecordReceiptModelViewController(RecordReceiptModelController recordReceiptModelController) {
		this.recordReceiptModelController = recordReceiptModelController;
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/{id}")
	public String recordSelected(@PathVariable(name = "customerId", required = true) Long customerId,
			@PathVariable(name = "historyId", required = true) Long historyId,
			@PathVariable(name = "recordId", required = true) Long recordId,
			@PathVariable(name = "id", required = true) Long receiptId,
			Model model) {

		recordReceiptModelController.init() // Re-initialize Model
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("recordId", recordId)
			.addControllerParam("receiptId", receiptId)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/record_receipt";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String recordCreate(@PathVariable(name = "customerId", required = true) Long customerId,
			@PathVariable(name = "historyId", required = true) Long historyId,
			@PathVariable(name = "recordId", required = true) Long recordId,
			Model model) {

		recordReceiptModelController.init() // Re-initialize Model
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("recordId", recordId)
			.addControllerParam("viewType", Actions.CREATE)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/record_receipt";
	}
	
	/**
	 * @param model
	 * @return
	 */
	@PostMapping("/{id}/pay")
	public String recordCreate(@PathVariable(name = "customerId", required = true) Long customerId,
			@PathVariable(name = "historyId", required = true) Long historyId,
			@PathVariable(name = "recordId", required = true) Long recordId,
			@PathVariable(name = "id", required = true) Long receiptId,
			@ModelAttribute RecordReceipt receipt,
			Model model) {

		recordReceiptModelController.init() // Re-initialize Model
			.addControllerParam("customerId", customerId)
			.addControllerParam("historyId", historyId)
			.addControllerParam("recordId", recordId)
			.addControllerParam("receiptId", receiptId)
			.addModelAttributes(receipt)
			.addControllerParam("action", Actions.EDIT)
			.addControllerParam("viewType", Actions.VIEW)
			.setViewModel(model)
			.run(); // GetValuesForView

		return "demo_1/pages/record_receipt";
	}
	
}
