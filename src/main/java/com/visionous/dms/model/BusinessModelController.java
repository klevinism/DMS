package com.visionous.dms.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.service.BusinessService;

@Controller
public class BusinessModelController extends ModelControllerImpl{
	
	private BusinessService businessService;
	
	/**
	 * @param businessService
	 */
	@Autowired
	public BusinessModelController(BusinessService businessService) {
		this.businessService = businessService;
	}
	
	@Override
	public void run() {
		// If action occurred, persist object to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				if(super.hasResultBindingError()) {
					super.setControllerParam("viewType", super.getAllControllerParams().get("action").toString().toLowerCase());
				}else {
					persistModelAttributes(
						(Business) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
				}
			}
		}
		
		// Build view
		this.buildBusinessViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Customer
		this.buildBusinessGlobalViewModel();
	}

	private void buildBusinessGlobalViewModel() {
		super.addModelCollectionToView("businesses", AccountUtil.currentLoggedInUser().getBusinesses());
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("currentUser", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInUser());
		super.addModelCollectionToView("settings", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInUser());
	}

	private void persistModelAttributes(Business business, String viewType) {
		// TODO Auto-generated method stub
		
	}

	private void buildBusinessViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
		}else if(viewType.equals(Actions.DELETE.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(AccountUtil.currentLoggedInUser() != null) {
				if(super.getAllControllerParams().containsKey("practice")) {
					Long businessId = (Long) super.getAllControllerParams().get("practice");
					
					Optional<Business> currentBusiness = AccountUtil.currentLoggedInUser()
							.getBusinesses()
							.stream()
							.filter(business -> business.getId() == businessId)
							.findFirst();
					if(currentBusiness.isPresent()) {
						AccountUtil.currentLoggedInUser().setCurrentBusiness(currentBusiness.get());	
					}else {
						super.getBindingResult().addError(new ObjectError("practiceNotFound", null, null, "Practice not found! Redirecting.."));
					}
				}
			}
		}
		
	}
	
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> BusinessModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
}
