package com.visionous.dms.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Role;
import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.service.RoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.PersonnelService;

/**
 * @author delimeta
 *
 */
@Controller
public class AccountModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(AccountModelController.class);

	private AccountService accountService;
	private RoleService roleService;
	private PersonnelService personnelService;
	private CustomerService customerService;
	private MessageSource messageSource;
	
	private static String currentPage = LandingPages.ACCOUNT.value();

	/**
	 *
	 * @param accountService
	 * @param roleService
	 * @param messageSource
	 * @param personnelService
	 * @param customerService
	 */
	@Autowired
	public AccountModelController(AccountService accountService, RoleService roleService,
								  MessageSource messageSource, PersonnelService personnelService,
								  CustomerService customerService) {
		
		this.accountService = accountService;
		this.roleService = roleService;
		this.messageSource = messageSource;
		this.personnelService = personnelService;
		this.customerService = customerService;
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
		this.buildAccountViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
			
		// Build global view model for Personnel
		this.buildAccountGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(Account accountNewModel, String action) {
		Account newAccount = accountNewModel;
		
		String messageEmailExists = messageSource.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messageSource.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
        
		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Account> oldAccount = accountService.findByIdAndBusinesses_Id(newAccount.getId(), AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
			
			if(!newAccount.getRoles().isEmpty()) {
				Optional<Role> role = roleService.findByName(newAccount.getRoles().get(0).getName());
				
				role.ifPresent(singleRole -> {
					newAccount.setRoles(new ArrayList<>());
					newAccount.addRole(singleRole);
				});			
			}

			oldAccount.ifPresent(oldacc -> {
				newAccount.setPassword(oldacc.getPassword());
			});

			Optional<Customer> customer = customerService.findById(newAccount.getId());

			try {
				if(oldAccount.isPresent() && 
						(oldAccount.get().getEmail().equals(newAccount.getEmail()) || 
						oldAccount.get().getUsername().equals(newAccount.getUsername()))
						) { // Has same username/email after account edit
					setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), newAccount, oldAccount.get(), customer.isPresent());
					Account created = accountService.createPlain(newAccount);
					
					if(created != null && AccountUtil.currentLoggedInUser().getId().equals(created.getId())) { // Self Edit
						AccountUtil.currentLoggedInUser().setAccount(created);
					}
					
				}else {
					accountService.update(newAccount);
				}
			} catch (EmailExistsException e) {
				super.getBindingResult().addError(new FieldError("account", "account.email", newAccount.getEmail(), false, null, null, messageEmailExists));
				logger.error(messageEmailExists);
				
		        super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.EDIT.getValue());
			} catch (UsernameExistsException e) {
				super.getBindingResult().addError(new FieldError("account", "account.username", newAccount.getUsername(), false, null, null, messageUsernameExists));
				logger.error(messageUsernameExists);
				
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.EDIT.getValue());
			}
		}else if(action.equals(Actions.CREATE.getValue())) {}
	}
	
	private String uploadProfileImage(MultipartFile file, String path) throws IOException {
		return FileManager.uploadImage(file, path);
	}
	
	private String uploadNewProfileImage(MultipartFile file, boolean isCustomer) throws IOException {
		if(isCustomer) {
			return uploadProfileImage(file, FileManager.CUSTOMER_PROFILE_IMAGE_PATH);
		}else {
			return uploadProfileImage(file, FileManager.PERSONNEL_PROFILE_IMAGE_PATH);
		}
	}
	
	private void setImageToAccount(MultipartFile file, Account newAccount, Account oldAccount, boolean isCustomer) {
		MultipartFile uploadedFile = file;
		if(uploadedFile != null && uploadedFile.getOriginalFilename() != null && !uploadedFile.getOriginalFilename().isEmpty()) {
			try {
				newAccount.setImage(uploadNewProfileImage((MultipartFile)super.getAllControllerParams().get("profileimage"), isCustomer));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			newAccount.setImage(oldAccount.getImage());
		}
	}

	/**
	 * 
	 */
	private void buildAccountViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			Personnel newPersonnel = new Personnel();
//			newPersonnel.setAccount(new Account());
			super.addModelCollectionToView("account", newPersonnel);
			
			if(super.hasResultBindingError()) {
				if(super.getAllControllerParams().containsKey("modelAttribute")) {	
					Account account = (Account) super.getAllControllerParams().get("modelAttribute");
					setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), account, account, false);
				}
			} 
		}else if(viewType.equals(Actions.EDIT.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				
				Long accountId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				Optional<Account> oldAccount = accountService.findByIdAndBusinesses_Id(accountId, AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());

				Role[] loggedInRoles = AccountUtil.currentLoggedInUser().getRoles().stream().toArray(Role[]::new);
				
				oldAccount.ifPresent(account -> {
					super.addModelCollectionToView("account", account);
					
					if(!account.getRoles().isEmpty()) {
						if(!account.getUsername().equals(AccountUtil.currentLoggedInUser().getUsername())) {
							if((loggedInRoles[0].getName().equals("CUSTOMER") && account.getRoles().get(0).getName().equals("CUSTOMER")) || 
								(loggedInRoles[0].getName().equals("PERSONNEL") && (account.getRoles().get(0).getName().equals("ADMIN") || account.getRoles().get(0).getName().equals("PERSONNEL")))) {
						        String errorEditingAccount = messageSource.getMessage("alert.errorEditingAccount", null, LocaleContextHolder.getLocale());
								super.addModelCollectionToView("errorEditingAccount", errorEditingAccount);
							}else {
								super.addModelCollectionToView("account", account);
							}
						}else {
							super.addModelCollectionToView("account", account);
						}
					}else {
						String errorEditingAccount = messageSource.getMessage("alert.errorEditingAccount", null, LocaleContextHolder.getLocale());
						super.addModelCollectionToView("errorEditingAccount", errorEditingAccount);
					}
				});
			}


			if(super.hasResultBindingError()) {
				if(super.getAllControllerParams().containsKey("modelAttribute")) {	
					Account account = (Account) super.getAllControllerParams().get("modelAttribute");
					Optional<Personnel> personnel = personnelService.findById(account.getId());
					setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), account, account, !personnel.isPresent());
				}
			} 
			
			super.addModelCollectionToView("allRoles", roleService.findAll());
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long accountId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				
				//Either personnel or customer. not both
				Optional<Account> selectedAccount = accountService.findByIdAndBusinesses_Id(accountId, AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
				selectedAccount.ifPresent(account -> {
					boolean isPersonnel = account.getRoles().stream().anyMatch(role -> role.getName().equals("PERSONNEL"));
					boolean isCustomer = account.getRoles().stream().anyMatch(role -> role.getName().equals("CUSTOMER"));

					super.addModelCollectionToView("account", account);

					if(isPersonnel){
						Optional<Personnel> personnel = personnelService.findById(account.getId());
						personnel.ifPresent(p -> super.addModelCollectionToView("selected", p));
					}else if(isCustomer){
						Optional<Customer> customer = customerService.findById(account.getId());
						customer.ifPresent(c -> super.addModelCollectionToView("selected", c));
					}
				});
			}else {
				Long accountId = AccountUtil.currentLoggedInUser().getAccount().getId();
				Optional<Account> selectedAccount = accountService.findByIdAndBusinesses_Id(accountId, AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
				selectedAccount.ifPresent(a -> {
					super.addModelCollectionToView("account", a);
					Optional<Personnel> personnel = personnelService.findById(a.getId());
					personnel.ifPresentOrElse(p -> {
						super.addModelCollectionToView("selected", p);
					}, () -> addModelCollectionToView("selected", null));
				});
			}
		}
		
	}
    
	/**
	 * 
	 */
	private void buildAccountGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		
		super.addModelCollectionToView("accountList", accountService.findAll());

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessImage());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getActiveSubscription());

	}
	
	@Override
	public <T> AccountModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
