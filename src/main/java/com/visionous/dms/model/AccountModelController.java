/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Service
public class AccountModelController extends ModelController{
	
	private final Log logger = LogFactory.getLog(AccountModelController.class);

	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private PersonnelRepository personnelRepository;
	private CustomerRepository customerRepository;
	
	private MessageSource messageSource;
	
	private static String currentPage = LandingPages.ACCOUNT.value();


	/**
	 * @param personnelRepository
	 */
	@Autowired
	public AccountModelController(AccountRepository accountRepository, RoleRepository roleRepository, MessageSource messageSource, 
			PersonnelRepository personnelRepository,
			CustomerRepository customerRepository) {
		
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
		this.messageSource = messageSource;
		this.personnelRepository = personnelRepository;
		this.customerRepository = customerRepository;
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
						(Account) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
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
		
		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
				Long[] roleids = (Long[]) super.getAllControllerParams().get("roles");
				for(Long roleId: roleids) {
					roleRepository.findById(roleId).ifPresent(role -> {
						newAccount.addRole(role);
					});
				}
				if(newAccount.getCustomer().getId()  != null) {
					newAccount.setPersonnel(null);
				}else if(newAccount.getPersonnel().getId()  != null) {
					newAccount.setCustomer(null);
					newAccount.getPersonnel().setAccount(newAccount);
				}
				accountRepository.saveAndFlush(newAccount);
		}else if(action.equals(Actions.CREATE.getValue())) {
			
		}else if(action.equals(Actions.VIEW.getValue())) {
		}	
		
	}

	/**
	 * 
	 */
	private void buildAccountViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			Personnel newPersonnel = new Personnel();
			newPersonnel.setAccount(new Account());
			super.addModelCollectionToView("selected", newPersonnel);
		}else if(viewType.equals(Actions.EDIT.getValue())) {

			Account currentLoggedInAccount=AccountUtil.currentLoggedInUser();
			Long accountId = Long.valueOf(super.getAllControllerParams().get("id").toString());
			Optional<Account> oldAccount = accountRepository.findById(accountId);

			Role[] loggedInRoles = currentLoggedInAccount.getRoles().stream().toArray(Role[]::new);					
			Role[] oldRoles = oldAccount.get().getRoles().stream().toArray(Role[]::new);
			
			if(oldRoles.length > 0) {
				if((loggedInRoles[0].getName().equals("CUSTOMER") && !oldRoles[0].getName().equals("CUSTOMER")) || 
						(loggedInRoles[0].getName().equals("PERSONNEL") && (oldRoles[0].getName().equals("ADMIN") || oldRoles[0].getName().equals("PERSONNEL")))) {
					
			        String errorEditingAccount = messageSource.getMessage("alert.errorEditingAccount", null, LocaleContextHolder.getLocale());
					super.addModelCollectionToView("errorEditingAccount", errorEditingAccount);
				}else {				
					oldAccount.ifPresent(account -> super.addModelCollectionToView("selected", account));
					
					Iterable<Role> allRoles= roleRepository.findAll();
					super.addModelCollectionToView("allRoles", allRoles);
				}
			}else {
					oldAccount.ifPresent(account -> super.addModelCollectionToView("selected", account));

					Iterable<Role> allRoles= roleRepository.findAll(); 
					super.addModelCollectionToView("allRoles", allRoles);
			}
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long accountId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				
				//Either personnel or customer. not both
				Optional<Personnel> selectedPersonnel = personnelRepository.findById(accountId);
				selectedPersonnel.ifPresent(personnel -> super.addModelCollectionToView("selected", personnel));
				Optional<Customer> selectedCustomer = customerRepository.findById(accountId);
				selectedCustomer.ifPresent(customer -> super.addModelCollectionToView("selected", customer));
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
			
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		
		Iterable<Account> personnels = accountRepository.findAll();
		super.addModelCollectionToView("accountList", personnels);
	}
	
	@Override
	public <T> AccountModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
