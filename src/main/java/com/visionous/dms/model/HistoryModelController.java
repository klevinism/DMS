/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.History;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.HistoryService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.TeethService;

/**
 * @author delimeta
 *
 */
@Controller
public class HistoryModelController extends ModelControllerImpl{
	private final Log logger = LogFactory.getLog(HistoryModelController.class);
	private static String currentPage = LandingPages.HISTORY.value();

	private TeethService teethService;
	
	private HistoryService historyService;
	private RecordService recordService;
	private CustomerService customerService;
	private GlobalSettings globalSettings;
	
	@Autowired
	private HistoryModelController(TeethService teethService, CustomerService customerService, 
			HistoryService historyService, RecordService recordService, 
			GlobalSettings globalSettings) {
	
		this.teethService = teethService;
		
		this.historyService = historyService;
		this.recordService = recordService;
		this.customerService = customerService;
		this.globalSettings = globalSettings;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		// If action occurred, persist object to db

		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				persistModelAttributes(
					(History) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
				);
			}
		}
		
		// Build view
		this.buildHistoryViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildHistoryGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(History historyNewModel, String action) {
		History newHistory = historyNewModel;

		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {

			if(super.getAllControllerParams().get("id") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());	
				
				if(AccountUtil.currentLoggedInUser().isPersonnel()) {
					newHistory.setSupervisor(AccountUtil.currentLoggedInUser().getPersonnel());	
				}
				
				customerService.createNewHistoryForCustomerId(customerId, newHistory);
			}
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}

	/**
	 * 
	 */
	private void buildHistoryViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
			Optional<Customer> selectedCustomer = customerService.findById(Long.valueOf(super.getAllControllerParams().get("id").toString()));
			selectedCustomer.ifPresent(customer->{
				if(customer.hasCustomerHistory()) {
					super.addModelCollectionToView("historyId", customer.getCustomerHistory().getId());
					super.addModelCollectionToView("selected", customer);	
				}
			});
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {

		}else if(viewType.equals(Actions.VIEW.getValue())) {
			Long customerId = (Long) super.getAllControllerParams().get("customerId");
			Optional<Customer> selectedCustomer = customerService.findById(customerId);
			selectedCustomer.ifPresent(single -> {
				super.addModelCollectionToView("selected", single);
				if(single.hasCustomerHistory()) {
					super.addModelCollectionToView("customerRecords", 
							recordService.findTop5ByHistoryIdOrderByServicedateDesc(single.getCustomerHistory().getId())
						);
				}
			});
			
			super.addModelCollectionToView("listTeeth", teethService.findAll());
		}
	}

	/**
	 * 
	 */
	private void buildHistoryGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		
		Iterable<History> histories = historyService.findAll();
		super.addModelCollectionToView("personnelList", histories);
		
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", globalSettings.getBusinessImage());		
	}
	
	
	/**
	 *
	 */
	@Override
	public <T> HistoryModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
