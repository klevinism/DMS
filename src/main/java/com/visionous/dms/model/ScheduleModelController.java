/**
 * 
 */
package com.visionous.dms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.service.AppointmentService;

/**
 * @author delimeta
 *
 */
@Controller
public class ScheduleModelController extends ModelControllerImpl{
	private static String currentPage = LandingPages.AGENDA.value();
	
	private GlobalSettings globalSettings;
	private AppointmentService appointmentService;
	/**
	 * 
	 */
	@Autowired
	public ScheduleModelController(GlobalSettings globalSettings, AppointmentService appointmentService) {
		this.globalSettings = globalSettings;
		this.appointmentService = appointmentService;
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
			List<Object[]> mostUsedFromServiceType = appointmentService.findTopAppointmentsByMostUsedServiceType();
			List<ServiceType> servicesMostUsed = new ArrayList<>(); 
			servicesMostUsed.addAll(
					mostUsedFromServiceType.stream().map(
							mapper -> {
									ServiceType ser = new ServiceType(); 
									ser.setId(Long.valueOf(mapper[0].toString()));
									ser.setName(mapper[1].toString()); 
									return ser;
								}).collect(Collectors.toList()));
			super.addModelCollectionToView("top5ServiceTypes", servicesMostUsed); 
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
