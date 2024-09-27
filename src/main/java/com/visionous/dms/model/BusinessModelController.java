package com.visionous.dms.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.lib.accounts.entity.Role;
import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.SubdomainExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.o2dent.lib.accounts.persistence.BusinessService;
import com.visionous.dms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnBusinessConfirmationEvent;
import com.visionous.dms.event.OnSubscriptionConfirmationEvent;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.SubscriptionHistory;

import javax.swing.text.html.Option;


@EnableJpaRepositories("com.*")
@EntityScan("com.*")
@Controller
public class BusinessModelController extends ModelControllerImpl{
	
	private static String currentPage = LandingPages.BUSINESS.value();
	private BusinessService businessService;
	private SubscriptionListService subscriptionListService;
	private SubscriptionHistoryService subscriptionHistoryService;
	private GlobalSettingsService globalSettingsService;
	private PersonnelService personnelService;
	private AccountService accountService;
	private ApplicationEventPublisher eventPublisher;
	private MessageSource messageSource;
	private RoleService roleService;
	
	/**
	 * @param businessService
	 */
	@Autowired
	public BusinessModelController(BusinessService businessService, SubscriptionHistoryService subscriptionHistoryService, 
			SubscriptionListService subscriptionListService, GlobalSettingsService globalSettingsService, 
			PersonnelService personnelService, AccountService accountService, RoleService roleService,
			ApplicationEventPublisher eventPublisher, MessageSource messageSource) {
		
		this.businessService = businessService;
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.globalSettingsService = globalSettingsService;
		this.personnelService = personnelService;
		this.accountService = accountService;
		this.subscriptionListService = subscriptionListService;
		this.eventPublisher = eventPublisher;
		this.messageSource = messageSource;
		this.roleService = roleService;
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
        Pattern txtAndNr_pattern = Pattern.compile("^[a-zA-Z0-9_ ]+$");

        String errorBusinessName = messageSource.getMessage("alert.businessNameNotCorrect", null, LocaleContextHolder.getLocale());
        String errorBusinessSubdomain = messageSource.getMessage("alert.businessSubdomainNotCorrect", null, LocaleContextHolder.getLocale());
        String errorBusinessSubdomainExists = messageSource.getMessage("alert.businessSubdomainExists", null, LocaleContextHolder.getLocale());

        
		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			
	        if(Objects.nonNull(business.getName()) 
	        		&& !txtAndNr_pattern.matcher(business.getName()).find()) {
	        	super.getBindingResult().addError(
						new FieldError("name", "name", business.getName(), false, null, null, errorBusinessName+"!"));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
				return ;
	        }
	        
	        if(Objects.nonNull(business.getSubdomainUri()) 
	        		&& !txt_pattern.matcher(business.getSubdomainUri()).find()) {
	        	super.getBindingResult().addError(
						new FieldError("subdomainUri", "subdomainUri", business.getSubdomainUri(), false, null, null, errorBusinessSubdomain+"!"));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
				return ;
	        }
	        
	        if(Objects.isNull(business.getAccounts()) || business.getAccounts().isEmpty()) {
	        	business.setAccounts(new HashSet<Account>());
	        }
	        
        	business.getAccounts().add(AccountUtil.currentLoggedInUser().getAccount());

	        try {
	        	business.setEnabled(true);

		        Business newBusiness = businessService.create(business);

				AccountUtil.setCurrentLoggedInBusiness(newBusiness);

		        Optional<Account> newAccount = accountService.findById(AccountUtil.currentLoggedInUser().getAccount().getId());
		        newAccount.ifPresent(account -> {
					Optional<Personnel> hasPersonnel = personnelService.findById(account.getId());
					if(!hasPersonnel.isPresent()){
						Personnel newPersonnel = new Personnel();
						newPersonnel.setType("ADMIN");
						newPersonnel.setId(account.getId());
						personnelService.update(newPersonnel);
						AccountUtil.currentLoggedInUser().setPersonnel(true);
					}

					if(account.getRoles().isEmpty()){
						Optional<Role> role = roleService.findByName("ADMIN");
						role.ifPresent(admin -> {
							account.addRole(admin);
							accountService.createPlain(account);
						});
					}
		        });
				

		        GlobalSettings globalSettingsDraft = new GlobalSettings();
		        globalSettingsDraft.setAppointmentTimeSplit(30);
		        globalSettingsDraft.setBusinessDays("1,6");
		        globalSettingsDraft.setBusinessEmail("business@email.com");
		        globalSettingsDraft.setBusinessName(newBusiness.getName());
		        globalSettingsDraft.setBusinessTimes("07:00,18:00");
				globalSettingsDraft.setBusinessId(newBusiness.getId());
		        
		        if(Objects.isNull(globalSettingsDraft.getSubscriptionHistorySet())) {
		        	globalSettingsDraft.setSubscriptionHistorySet(new HashSet<>());
		        }
		        
		        globalSettingsDraft.setBusinessId(newBusiness.getId());
		        GlobalSettings newGlobalSettings = this.globalSettingsService.update(globalSettingsDraft);

				SubscriptionHistory subscriptionHistoryDraft = new SubscriptionHistory();
		        subscriptionHistoryDraft.setActive(true);
		        subscriptionHistoryDraft.setAddeddate(LocalDateTime.now());
		        subscriptionHistoryDraft.setBusinessId(newBusiness.getId());
		        subscriptionHistoryDraft.setSubscriptionStartDate(LocalDateTime.now());
		        subscriptionHistoryDraft.setSubscriptionEndDate(LocalDateTime.now().plusDays(31));
		        
		        subscriptionListService.findByName("Free").ifPresent(freeSubscription -> {
			        subscriptionHistoryDraft.setSubscription(freeSubscription);
					subscriptionHistoryDraft.setSubscriptionId(freeSubscription.getId());
		        });

				newGlobalSettings.setSubscriptionHistorySet(Set.of(subscriptionHistoryDraft));
		        subscriptionHistoryDraft.setGlobalSettings(newGlobalSettings);
//				Optional<SubscriptionHistory> subscriptionHistory =
//						subscriptionHistoryService.findActiveSubscriptionByBusinessId(newGlobalSettings.getBusiness().getId());
//
//				subscriptionHistory.ifPresent(history -> {
//
//				});
		        
		        SubscriptionHistory subscriptionHistory = this.subscriptionHistoryService.update(subscriptionHistoryDraft);
				AccountUtil.currentLoggedInUser().setCurrentBusinessSettings(subscriptionHistory.getGlobalSettings());
//				AccountUtil.currentLoggedInBusinessSettings().getSubscriptionHistorySet().add(subscriptionHistory);

		        super.addModelCollectionToView("account", AccountUtil.currentLoggedInUser().getAccount());
		        super.addModelCollectionToView("createdBusiness", AccountUtil.currentLoggedInBussines());
		        
		        eventPublisher.publishEvent(
		        		new OnBusinessConfirmationEvent(
		        				AccountUtil.currentLoggedInUser().getAccount(),
								newBusiness,
		        				LocaleContextHolder.getLocale(), 
		        				DmsCore.appMainPath())
		        		);
		        
		        eventPublisher.publishEvent(
		        		new OnSubscriptionConfirmationEvent(
		        				AccountUtil.currentLoggedInUser().getAccount(),
								newBusiness,
								(GlobalSettings) AccountUtil.currentLoggedInUser().getCurrentBusinessSettings(),
		        				LocaleContextHolder.getLocale(), 
		        				DmsCore.appMainPath())
		        		);

	        }catch(SubdomainExistsException e) {
	        	super.getBindingResult().addError(
						new FieldError("business", "business.subdomainUri", business.getSubdomainUri(), false, null, null, errorBusinessSubdomainExists));
								
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
	        }
			
		}else if(action.equals(Actions.VIEW.getValue())) {
		}
	}

	private void buildBusinessViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			if(!super.getAllControllerParams().containsKey("modelAttribute")) {
	
				Business newBusiness = new Business();
				newBusiness.setAccounts(new HashSet<Account>());
				newBusiness.getAccounts().add(AccountUtil.currentLoggedInUser().getAccount());
				
		        super.addModelCollectionToView("business", newBusiness);

			}
		}else if(viewType.equals(Actions.DELETE.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(AccountUtil.currentLoggedInUser() != null) {
				if(super.getAllControllerParams().containsKey("practice")) {
					Long businessId = (Long) super.getAllControllerParams().get("practice");
					
					Optional<Business> currentBusiness = AccountUtil.currentLoggedInUser()
							.getAccount()
							.getBusinesses()
							.stream()
							.filter(business -> business.getId() == businessId)
							.findFirst();
					if(currentBusiness.isPresent()) {
						AccountUtil.currentLoggedInUser().setCurrentBusiness(currentBusiness.get());	
					}else {
						super.getBindingResult().addError(new ObjectError("practiceNotFound", null, null, "Practice not found! Redirecting.."));
					}
				}else{

				}
			}
		}
		
		if(super.hasResultBindingError()) {
			if(!super.getAllControllerParams().containsKey("modelAttribute")) {
				super.clearResultBindingErrors();
			}
		}
		
	}
	
	private void buildBusinessGlobalViewModel() {
		super.addModelCollectionToView("businesses", AccountUtil.currentLoggedInUser().getAccount().getBusinesses());
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("currentUser", AccountUtil.currentLoggedInUser());
		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());

		if(!Objects.isNull(AccountUtil.currentLoggedInBusinessSettings())) {
			super.addModelCollectionToView("settings", AccountUtil.currentLoggedInBusinessSettings());
			super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
			super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());
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
