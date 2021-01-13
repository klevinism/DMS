/**
 * 
 */
package com.visionous.dms.model;

import java.util.Date;
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
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Service
public class CustomerModelController extends ModelController{
	
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	
	private static String currentPage = LandingPages.CUSTOMER.value();

	
	/**
	 * @param customerRepository
	 */
	@Autowired
	public CustomerModelController(CustomerRepository customerRepository, AccountRepository accountRepository, RoleRepository roleRepository) {
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;	
		this.roleRepository = roleRepository;
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
					(Customer) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
					);
			}
		}
		
		// Build view
		this.buildCustomerViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Customer
		this.buildCustomerGlobalViewModel();
	}

	/**
	 * 
	 */
	private void persistModelAttributes(Customer customerNewModel, String action) {
		Customer newCustomer = customerNewModel;
		
		if(action.equals(Actions.DELETE.getValue())) {

			Optional<Account> acc = accountRepository.findById(newCustomer.getId());		

			acc.ifPresent(account->{
				logger.debug(" Deleting new Customer with username = " + account.getUsername());

				account.setPersonnel(null);
				account.setCustomer(null);
				account.setRoles(null);
				accountRepository.delete(account);
			});
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {

			Optional<Account> acc = accountRepository.findById(newCustomer.getId());
			acc.ifPresent(account -> {
				logger.debug(" Editing new Customer with username = " + account.getUsername());

				newCustomer.setAccount(account);
				newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
				account.setCustomer(newCustomer); 
				accountRepository.saveAndFlush(account);
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			logger.debug(" Creating new Customer with username = " + newCustomer.getAccount().getUsername());

			if(super.getAllControllerParams().get("roles") != null) {
				String[] roleids = (String[]) super.getAllControllerParams().get("roles");
				
				newCustomer.getAccount().setCustomer(null);
				newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
				newCustomer.getAccount().setPersonnel(null);
				
				for(String roleName: roleids) {
					roleRepository.findByName(roleName).ifPresent(role -> {newCustomer.getAccount().addRole(role);});
				}
				
				Account newAccount = accountRepository.saveAndFlush(newCustomer.getAccount());
				newCustomer.setAccount(newAccount);				
				customerRepository.saveAndFlush(newCustomer);
			}
		}else if(action.equals(Actions.VIEW.getValue())) {
			
		}		

	}

	/**
	 * 
	 */
	private void buildCustomerViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			Customer newCustomer = new Customer();
			newCustomer.setAccount(new Account());
			super.addModelCollectionToView("selected", newCustomer);
			
			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String customerId = super.getAllControllerParams().get("id").toString();
			Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
			customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));

			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String customerId = super.getAllControllerParams().get("id").toString();
				Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
				customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));

				Iterable<Role> allRoles = roleRepository.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void buildCustomerGlobalViewModel() {
		HttpServletRequest path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		super.addModelCollectionToView("currentPagePath", path.getRequestURI());
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		

		Iterable<Customer> personnels = customerRepository.findAll();
		super.addModelCollectionToView("customerList", personnels);
	}
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> CustomerModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
