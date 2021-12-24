/**
 * 
 */
package com.visionous.dms.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.service.SubscriptionHistoryService;
import com.visionous.dms.service.SubscriptionListService;

/**
 * @author delimeta
 *
 */
@Controller
public class SubscriptionModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(SubscriptionModelController.class);
	
	private static String currentPage = LandingPages.SUBSCRIPTION.value();
	
	private SubscriptionListService subscriptionListService;
	
	private SubscriptionHistoryService subscriptionHistoryService;

	/**
	 * 
	 */
	@Autowired
	public SubscriptionModelController(SubscriptionListService subscriptionListService, 
			SubscriptionHistoryService subscriptionHistoryService) {
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.subscriptionListService = subscriptionListService;
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
					(Record) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
				);
			}
		}
		
		// Build view
		this.buildRecordViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildRecordGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(Record recordNewModel, String action) {
		Record newRecord = recordNewModel;

		if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {
		}
	}
	
	/**
	 * 
	 */
	private void buildRecordViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
		}else if(viewType.equals(Actions.VIEW.getValue())) {

			this.subscriptionHistoryService.findActiveSubscriptionByBusinessId(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId()).ifPresent(
					subscription -> super.addModelCollectionToView("currentSubscription", subscription));
		}
		
	}
	
	/**
	 * 
	 */
	private void buildRecordGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("subscriptionList", this.subscriptionListService.findAllOrderedByIdAsc());
		super.addModelCollectionToView("subscriptionHistory", this.subscriptionHistoryService.findAllByBusinessIdOrderedBySubscriptionEndDateDesc(AccountUtil.currentLoggedInBussines().getId()));
		
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessImage());
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBussines().getActiveSubscription().getSubscription());
	}
	
	@Override
	public <T> SubscriptionModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
