/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
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
	
	private static String currentPage = LandingPages.ACCOUNT.value();


	/**
	 * @param personnelRepository
	 */
	@Autowired
	public AccountModelController(AccountRepository accountRepository, RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		buildAccountGlobalViewModel();
		
		// If action occured, add attributes to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			persistModelAttributes();
		}
		
		// Build view
		if(!super.getAllControllerParams().containsKey("modal") &&
				super.getAllControllerParams().containsKey("action")) {
			buildAccountActionViewModel();
		}else {
			buildAccountViewModel();
			
			if(super.getAllControllerParams().containsKey("action")) {
				buildAccountActionViewModel();	
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes() {
		Account newAccount = (Account)super.getAllControllerParams().get("modelAttribute");
		String[] roleids = (String[]) super.getAllControllerParams().get("roles");
		for(String roleName: roleids) {
			roleRepository.findByName(roleName).ifPresent(role -> {System.out.println(roleName);newAccount.addRole(role);});
		}
		if(newAccount.getCustomer().getId()  != null) {
			System.out.println(" customer not null");
			newAccount.setPersonnel(null);
		}else if(newAccount.getPersonnel().getId()  != null) {
			System.out.println(" personnel not null");
			newAccount.setCustomer(null);
			newAccount.getPersonnel().setAccount(newAccount);
		}

		accountRepository.saveAndFlush(newAccount);
	}

	/**
	 * 
	 */
	private void buildAccountActionViewModel() {
		super.addModelCollectionToView("action",super.getAllControllerParams().get("action").toString().toLowerCase());
		
		if(super.getAllControllerParams().get("action").equals(Actions.CREATE)) {
			Personnel newPersonnel = new Personnel();
			newPersonnel.setAccount(new Account());
			super.addModelCollectionToView("selected", newPersonnel);
		}else {
			String personnelId = super.getAllControllerParams().get("id").toString();
			Optional<Account> account = accountRepository.findById(Long.valueOf(personnelId));
			account.ifPresent(x -> super.addModelCollectionToView("selected", account.get()));
			
			Iterable<Role> allRoles= roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
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
		
	}
	
	/**
	 * 
	 */
	private void buildAccountViewModel() {
		
		Iterable<Account> personnels = accountRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
	}
	
	@Override
	public <T> AccountModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
