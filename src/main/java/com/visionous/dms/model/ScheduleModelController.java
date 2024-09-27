/**
 * 
 */
package com.visionous.dms.model;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author delimeta
 *
 */
@Controller
public class ScheduleModelController extends ModelControllerImpl{
	private static String currentPage = LandingPages.AGENDA.value();
	
	private AppointmentService appointmentService;

	private AccountService accountService;

	/**
	 * 
	 */
	@Autowired
	public ScheduleModelController(AppointmentService appointmentService, AccountService accountService) {
		this.appointmentService = appointmentService;
		this.accountService = accountService;
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

			List<Account> personnelIds = accountService.findAllByBusinessIdAndRoles_NameIn(
					AccountUtil.currentLoggedInBussines().getId(), List.of("ADMIN","PERSONNEL"));

			List<Object[]> mostUsedFromServiceType = appointmentService
					.findTopAppointmentsByMostUsedServiceTypeAndPersonnelIdIn(
							personnelIds.stream().map(p -> p.getId()).collect(Collectors.toList()));
			
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

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

	}
	
	@Override
	public <T> ScheduleModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
	
}
