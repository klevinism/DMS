package com.visionous.dms.model;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.lib.accounts.entity.Role;
import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.o2dent.lib.accounts.persistence.BusinessService;
import com.visionous.dms.configuration.helpers.*;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.exception.SubscriptionException;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author delimeta
 *
 */
@Controller
public class PersonnelModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(PersonnelModelController.class);
	
	private RoleService roleService;
	private RecordService recordService;
    private ApplicationEventPublisher eventPublisher;
    private PersonnelService personnelService;
	private AccountService accountService;
    private MessageSource messages;
	private BusinessService businessService;
    
	private static String currentPage = LandingPages.PERSONNEL.value();

	/**
	 *
	 * @param roleService
	 * @param eventPublisher
	 * @param recordService
	 * @param messages
	 * @param personnelService
	 * @param businessService
	 */
	@Autowired
	public PersonnelModelController(RoleService roleService, ApplicationEventPublisher eventPublisher,
									RecordService recordService, MessageSource messages,
									PersonnelService personnelService, BusinessService businessService,
									AccountService accountService) {
	
		this.roleService = roleService;
		this.eventPublisher = eventPublisher;
		this.recordService = recordService;
		this.messages = messages;
		this.personnelService = personnelService;
		this.businessService = businessService;
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
							(Personnel) super.getAllControllerParams().get("modelAttribute"), 
							super.getAllControllerParams().get("action").toString().toLowerCase()
					);
				}
			}
		}

		// Build view
		this.buildPersonnelViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildPersonnelGlobalViewModel();
	}

	/**
	 * 
	 */
	private void persistModelAttributes(Personnel personnelNewModel, String action) {
		Personnel newPersonnel = personnelNewModel;

		String messageEmailExists = messages.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messages.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
        String messagePhoneNumberExists = messages.getMessage("alert.phoneNumberExists", null, LocaleContextHolder.getLocale());

        String subscriptionReachLimit = messages.getMessage("SubscriptionReachLimit", null, LocaleContextHolder.getLocale());
        String subscriptionLimitUpgrade = messages.getMessage("SubscriptionLimitUpgrade", null, LocaleContextHolder.getLocale());
		
		if(action.equals(Actions.DELETE.getValue())) {
			accountService.disableById(newPersonnel.getId());
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			Optional<Account> newAccount = accountService.findById(newPersonnel.getId());

			if(newAccount.isPresent() && newAccount.get().getRoles().get(0).getName().equals("PERSONNEL")) {

				String passPlain = newAccount.get().getPassword();

				try {
					String imageName = null;
					if((imageName = this.upladProfileImage()) != null) {
						newAccount.get().setImage(imageName);
					}

					if(!reachedSubscriptionLimit()) {
						Business loggedInBusiness = AccountUtil.currentLoggedInBussines();
						loggedInBusiness.getAccounts().add(newAccount.get());
						newAccount.get().addBusiness(loggedInBusiness);
						
						Personnel createdPersonnel = personnelService.create(newPersonnel);
						Business updatedBusiness = businessService.update(loggedInBusiness);

						newAccount.get().setPassword(passPlain);
						this.publishNewAccountEvent(newAccount.get());
					}else {
						throw new SubscriptionException(subscriptionReachLimit + " " + subscriptionLimitUpgrade);
					}
				} catch (IOException e) {
					logger.error(e.getMessage());
				} catch (EmailExistsException e) {
					super.getBindingResult().addError(
							new FieldError("account", "account.email", newAccount.get().getEmail(), false, null, null, messageEmailExists)
						);

			        super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
			        logger.error(messageEmailExists);
				} catch (UsernameExistsException e) {
					super.getBindingResult().addError(
							new FieldError("account", "account.username", newAccount.get().getEmail(), false, null, null, messageUsernameExists)
						);

				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
					logger.error(messageUsernameExists);
				} catch (SubscriptionException e) {
					if(!super.hasResultBindingError()) {
						super.getBindingResult().addError(
								new FieldError("subscriptionLimit", "subscription", e.getMessage(), false, null, null, e.getMessage())
							);
					}
					
					super.addModelCollectionToView("subscriptionLimit", e.getMessage());
				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				} catch (PhoneNumberExistsException e) {

					super.getBindingResult().addError(
							new FieldError("account", "account.phone", newAccount.get().getPhone(), false, null, null, messageUsernameExists)
						);

				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
					logger.error(messagePhoneNumberExists);
				}
			}
		}else if(action.equals(Actions.VIEW.getValue())) {

		}		

	}
	
	private boolean reachedSubscriptionLimit() {
		int personnelSize = this.accountService.findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_NameIn(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), true,true, List.of("PERSONNEL")).size();
		
		if(AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription() == null) {
			return false;
		}
		
		if(AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription().hasRestrictionsByPageName(currentPage)) {
			int subscriptionRestrictionSize = AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription().getSumOfRestrictionsAmountByPageName(currentPage);
			return personnelSize >= subscriptionRestrictionSize;
		}else {
			return false;
		}
	}
	
	private boolean nearSubscriptionLimitBy(int count) {
		int personnelSize = this.accountService.findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_NameIn(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), true, true, List.of("PERSONNEL")).size();

		if(AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription() == null) {
			return false;
		}
		
		if(AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription().hasRestrictionsByPageName(currentPage)) {
			int subscriptionRestrictionSize = AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription().getSumOfRestrictionsAmountByPageName(currentPage);
			return (subscriptionRestrictionSize - personnelSize) == count;
		}else {
			return false;
		}


	}
	
	private void publishNewAccountEvent(Account account) {
		eventPublisher.publishEvent(
        		new OnRegistrationCompleteEvent(account, LocaleContextHolder.getLocale(), DmsCore.appMainPath())
    		);
	}
	
	
	/**
	 * Returns the name of the saved profile image, 
	 * or null if uploaded file has no name
	 * 
	 * @return imageFileName String
	 * @throws IOException
	 */
	private String upladProfileImage() throws IOException {
		if(super.getAllControllerParams().containsKey("profileimage")) {
			MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("profileimage");
			if(uploadedFile.getOriginalFilename() != null && !uploadedFile.isEmpty()){
				return FileManager.write(uploadedFile, "/tmp/personnel/profile/");
			}
		}
		return null;
	}
	/**
	 * 
	 */
	private void buildPersonnelViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {

			if(super.getModelCollectionToView("personnel") == null) {
				Personnel newPersonnel = new Personnel();
//				newPersonnel.setAccount(new Account());
				super.addModelCollectionToView("personnel", newPersonnel);
			}
						
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isPersonnelCreation", true);
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String personnelId = super.getAllControllerParams().get("id").toString();
			Optional<Account> selectedAccount = accountService.findByIdAndBusinesses_Id(Long.valueOf(personnelId), AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
			selectedAccount.ifPresent(a -> {
				Optional<Personnel> personnel = personnelService.findById(a.getId());
				personnel.ifPresent(p -> {
					super.addModelCollectionToView("selected", p);
				});
			});

			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String personnelId = super.getAllControllerParams().get("id").toString();

				Optional<Account> selectedAccount = accountService.findByIdAndBusinesses_Id(Long.valueOf(personnelId), AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
				selectedAccount.ifPresent(a -> {
					Optional<Personnel> personnel = personnelService.findById(a.getId());
					personnel.ifPresent(p -> {
						super.addModelCollectionToView("selected", p);
					});
				});

				Iterable<Role> allRoles = roleService.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}

			if(!reachedSubscriptionLimit() && nearSubscriptionLimitBy(1)) {
				super.addModelCollectionToView("subscriptionNearLimit", nearSubscriptionLimitBy(1));
			}else{
				super.addModelCollectionToView("subscriptionReachLimit", reachedSubscriptionLimit());
			}
		}
	}
	
	/**
	 * 
	 */
	private void buildPersonnelGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		
		List<Account> accountList = accountService.findAllByBusinessIdAndRoles_Name(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), "PERSONNEL");
		List<Personnel> personnelList = personnelService.findByIdIn(accountList.stream().map(a -> a.getId()).collect(Collectors.toList()));

		super.addModelCollectionToView("personnelList", accountList);

		Date today = new Date();
		Date lastMonthEnd = DateUtil.getEndingOfMonth(DateUtil.getOneMonthBefore(today));
		Date lastMonthBegin = DateUtil.getBeginingOfMonth(DateUtil.getOneMonthBefore(today));
		Date lastLastMonthEnd = DateUtil.getEndingOfMonth(DateUtil.getMonthsBefore(today, 2));
		Date lastLastMonthBegin = DateUtil.getBeginingOfMonth(DateUtil.getMonthsBefore(today, 2));
		
		List<Double> monthlyVisitPerc = new ArrayList<>();
		personnelList.forEach(personnel->{
			int cntLastMonth = 0;
			double perc = 0.0;
			cntLastMonth = recordService.countByPersonnelIdAndServicedateBetween(personnel.getId(), 
					new Timestamp(lastMonthBegin.getTime()).toLocalDateTime(), new Timestamp(lastMonthEnd.getTime()).toLocalDateTime());
			int cntLastLastMonth = 0;
			cntLastLastMonth = recordService.countByPersonnelIdAndServicedateBetween(personnel.getId(), 
					new Timestamp(lastLastMonthBegin.getTime()).toLocalDateTime(), new Timestamp(lastLastMonthEnd.getTime()).toLocalDateTime());
			int diff = cntLastMonth - cntLastLastMonth;
			try {
				if(cntLastMonth != 0) {
					double divByLastMonth = diff/cntLastMonth;
					perc = divByLastMonth*100;
				}
				if(cntLastMonth == 0) {
					perc = diff*100;
				}
			}catch(ArithmeticException e) {
				e.printStackTrace();
			}

			monthlyVisitPerc.add(perc);
			super.addModelCollectionToView("monthlyGrowth", monthlyVisitPerc);
		});

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
	
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		
		super.addModelCollectionToView("settings", AccountUtil.currentLoggedInBusinessSettings());

		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

	}
	
	@Override
	public <T> PersonnelModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
