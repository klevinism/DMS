/**
 * 
 */
package com.visionous.dms.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.FileManager;
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
@Controller
public class AccountModelController extends ModelControllerImpl{
	
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
		
		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Account> oldAccount = accountRepository.findById(newAccount.getId());

			if(newAccount.getCustomer().getId()  != null) {
				newAccount.setPersonnel(null);
			}else if(newAccount.getPersonnel().getId()  != null) {
				newAccount.setCustomer(null);
			}
			
			if(!newAccount.getRoles().isEmpty()) {
				Optional<Role> role = roleRepository.findByName(newAccount.getRoles().get(0).getName().toString());
				role.ifPresent(singleRole -> {
					newAccount.setRoles(new ArrayList<>());
					newAccount.addRole(singleRole);
				});
				if(newAccount.getCustomer()  != null) {
					newAccount.getCustomer().setAccount(newAccount);
				}else if(newAccount.getPersonnel() != null) {
					newAccount.getPersonnel().setAccount(newAccount);
				}
			
			}

			oldAccount.ifPresent(oldacc -> {
				newAccount.setPassword(oldacc.getPassword());
				if(super.getAllControllerParams().get("profileimage") != null) {
					setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), newAccount, oldAccount.get());
				}
			});
			
			Date birthday = newAccount.getBirthday();
			Date today = new Date();
			Period period = DateUtil.getPeriodBetween(birthday, today);
			newAccount.setAge(period.getYears());
			
			accountRepository.saveAndFlush(newAccount);
		}else if(action.equals(Actions.CREATE.getValue())) {
			
		}else if(action.equals(Actions.VIEW.getValue())) {
		}	
		
	}
	
	private void setImageToAccount(MultipartFile file, Account newAccount, Account oldAccount) {
	
		MultipartFile uploadedFile = file;
		if(uploadedFile != null && uploadedFile.getOriginalFilename()!= null){
			if(!uploadedFile.getOriginalFilename().isEmpty()) {
				StringBuilder attachmentPath = new StringBuilder();
				try {
					String path = "";
					if(oldAccount.getCustomer() != null)
						path = FileManager.write(uploadedFile, "/tmp/customer/profile/");
					else
						path = FileManager.write(uploadedFile, "/tmp/personnel/profile/");
					
				    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
				    String fileName = date + uploadedFile.getOriginalFilename();
					attachmentPath.append(fileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				newAccount.setImage(attachmentPath.toString());
			}else {
				newAccount.setImage(oldAccount.getImage());
			}
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
			super.addModelCollectionToView("account", newPersonnel); 
		}else if(viewType.equals(Actions.EDIT.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long id = (Long) super.getAllControllerParams().get("id");
				Account currentLoggedInAccount=AccountUtil.currentLoggedInUser();
				Long accountId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				Optional<Account> oldAccount = accountRepository.findById(accountId);
	
				Role[] loggedInRoles = currentLoggedInAccount.getRoles().stream().toArray(Role[]::new);					
				
				oldAccount.ifPresent(account -> {
					super.addModelCollectionToView("account", account);
					
					if(!account.getRoles().isEmpty()) {
						if((loggedInRoles[0].getName().equals("CUSTOMER") && account.getRoles().get(0).getName().equals("CUSTOMER")) || 
							(loggedInRoles[0].getName().equals("PERSONNEL") && (account.getRoles().get(0).getName().equals("ADMIN") || account.getRoles().get(0).getName().equals("PERSONNEL")))) {
					        String errorEditingAccount = messageSource.getMessage("alert.errorEditingAccount", null, LocaleContextHolder.getLocale());
							super.addModelCollectionToView("errorEditingAccount", errorEditingAccount);
						}else {
							super.addModelCollectionToView("account", account);
						}
					}
				});
			}
			
			if(super.hasResultBindingError()) {
				if(super.getAllControllerParams().containsKey("modelAttribute")) {
					
					Account account = (Account) super.getAllControllerParams().get("modelAttribute");

					Optional<Account> oldAccount = accountRepository.findById(account.getId());
					oldAccount.ifPresent(oldaccount -> {
						setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), account, oldaccount);
						
					});
				}
			}
			Iterable<Role> allRoles= roleRepository.findAll(); 
			super.addModelCollectionToView("allRoles", allRoles);
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

		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
	}
	
	@Override
	public <T> AccountModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
