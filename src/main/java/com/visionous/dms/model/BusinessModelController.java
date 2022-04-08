package com.visionous.dms.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.PhoneNumberExistsException;
import com.visionous.dms.exception.SubdomainExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.SubscriptionHistory;
import com.visionous.dms.service.AccountService;
import com.visionous.dms.service.BusinessService;
import com.visionous.dms.service.GlobalSettingsService;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.SubscriptionHistoryService;
import com.visionous.dms.service.SubscriptionListService;

@Controller
public class BusinessModelController extends ModelControllerImpl{
	
	private static String currentPage = LandingPages.BUSINESS.value();
	
	private BusinessService businessService;

	private SubscriptionListService subscriptionListService;

	private SubscriptionHistoryService subscriptionHistoryService;

	private GlobalSettingsService globalSettingsService;

	private PersonnelService personnelService;
	
	private AccountService accountService;
	
	/**
	 * @param businessService
	 */
	@Autowired
	public BusinessModelController(BusinessService businessService, SubscriptionHistoryService subscriptionHistoryService, 
			SubscriptionListService subscriptionListService,
			GlobalSettingsService globalSettingsService, PersonnelService personnelService,
			AccountService accountService) {
		
		this.businessService = businessService;
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.globalSettingsService = globalSettingsService;
		this.personnelService = personnelService;
		this.accountService = accountService;
		this.subscriptionListService = subscriptionListService;
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

	private void persistModelAttributes(Business business, String action) {

        Pattern txt_pattern = Pattern.compile("^[a-z]+$");
        Pattern txtAndNr_pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        
		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			
	        if(Objects.nonNull(business.getName()) 
	        		&& !txtAndNr_pattern.matcher(business.getName()).find()) {
	        	super.getBindingResult().addError(
						new FieldError("business", "business.name", business.getName(), false, null, null, "Business name not coerrect!"));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
	        }
	        
	        if(Objects.nonNull(business.getSubdomainUri()) 
	        		&& !txt_pattern.matcher(business.getSubdomainUri()).find()) {
	        	super.getBindingResult().addError(
						new FieldError("business", "business.subdomainUri", business.getSubdomainUri(), false, null, null, "Business subdomainURI not coerrect!"));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
	        }
	        
	        if(Objects.isNull(business.getAccounts()) || business.getAccounts().isEmpty()) {
	        	business.setAccounts(new HashSet<Account>());
	        }
	        
        	business.getAccounts().add(AccountUtil.currentLoggedInUser().getAccount());

	        try {
	        	business.setEnabled(true);

		        Business newBusiness = businessService.create(business);

		        Optional<Account> newAccount = accountService.findById(AccountUtil.currentLoggedInUser().getId());
		        newAccount.ifPresent(account -> {
			        Personnel newPersonnel = new Personnel();
			        newPersonnel.setType("ADMIN");
			        
			        account.setPersonnel(newPersonnel);
			        newPersonnel.setAccount(account);

			        personnelService.update(newPersonnel);
		        });
				

		        GlobalSettings globalSettingsDraft = new GlobalSettings();
		        globalSettingsDraft.setAppointmentTimeSplit(30);
		        globalSettingsDraft.setBusinessDays("1,6");
		        globalSettingsDraft.setBusinessEmail("business@email.com");
		        globalSettingsDraft.setBusinessName("MyDentalClinic");
		        globalSettingsDraft.setBusinessTimes("07:00,18:00");
		        
		        if(Objects.isNull(globalSettingsDraft.getSubscriptions())) {
		        	globalSettingsDraft.setSubscriptions(new HashSet<>());
		        }
		        
		        globalSettingsDraft.setBusiness(business);
	        	
		        GlobalSettings newGlobalSettings = this.globalSettingsService.update(globalSettingsDraft);
	        	
		        SubscriptionHistory subscriptionHistoryDraft = new SubscriptionHistory();
		        subscriptionHistoryDraft.setActive(true);
		        subscriptionHistoryDraft.setAddeddate(LocalDateTime.now());
		        subscriptionHistoryDraft.setBusiness(business);
		        subscriptionHistoryDraft.setSubscriptionStartDate(LocalDateTime.now());
		        subscriptionHistoryDraft.setSubscriptionEndDate(LocalDateTime.now().plusDays(31));
		        
		        subscriptionListService.findByName("Free").ifPresent(freeSubscription -> {
			        subscriptionHistoryDraft.setSubscription(freeSubscription);
		        });
		        
		        subscriptionHistoryDraft.setGlobalSettings(newGlobalSettings);
		        newGlobalSettings.getSubscriptions().add(subscriptionHistoryDraft);
		        
		        SubscriptionHistory subscriptionHistory = this.subscriptionHistoryService.update(subscriptionHistoryDraft);
		        
	        }catch(SubdomainExistsException e) {
	        	super.getBindingResult().addError(
						new FieldError("business", "business.subdomainUri", business.getSubdomainUri(), false, null, null, "Business subdomainURI exists!"));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
	        }
			
		}else if(action.equals(Actions.VIEW.getValue())) {
		}
	}

	private void buildBusinessViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			Business newBusiness = new Business();
			newBusiness.setAccounts(new HashSet<Account>());
			newBusiness.getAccounts().add(AccountUtil.currentLoggedInUser().getAccount());
			
			super.addModelCollectionToView("business", newBusiness);
			
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
	
	private void buildBusinessGlobalViewModel() {
		super.addModelCollectionToView("businesses", AccountUtil.currentLoggedInUser().getBusinesses());
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("currentUser", AccountUtil.currentLoggedInUser());
		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInUser());
		super.addModelCollectionToView("settings", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInUser());
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
