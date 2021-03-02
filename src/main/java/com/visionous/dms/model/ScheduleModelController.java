/**
 * 
 */
package com.visionous.dms.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.GlobalSettings;

/**
 * @author delimeta
 *
 */
@Controller
public class ScheduleModelController extends ModelControllerImpl{
	private static String currentPage = LandingPages.AGENDA.value();
	
	private GlobalSettings globalSettings;
	
	/**
	 * 
	 */
	@Autowired
	public ScheduleModelController(GlobalSettings globalSettings) {
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
				if(super.hasResultBindingError()) {
					super.setControllerParam("viewType", super.getAllControllerParams().get("action").toString().toLowerCase());
				}else {
					persistModelAttributes(
						(Account) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
				}
			}
		}
	
	
		// Build view
		this.buildScheduleViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
			
		// Build global view model for Personnel
		this.buildScheduleGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(Account accountNewModel, String action) {
		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {}
	}
	
	/**
	 * 
	 */
	private void buildScheduleViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
		}else if(viewType.equals(Actions.EDIT.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			
		}
		
	}
    
	/**
	 * 
	 */
	private void buildScheduleGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", globalSettings.getBusinessImage());
	}
	
	@Override
	public <T> ScheduleModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
	
}
