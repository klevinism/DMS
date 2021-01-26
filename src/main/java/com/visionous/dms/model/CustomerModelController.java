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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.QuestionnaireRepository;
import com.visionous.dms.repository.QuestionnaireResponseRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.RoleRepository;
import com.visionous.dms.repository.TeethRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class CustomerModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private RecordRepository recordRepository;
	private QuestionnaireResponseRepository questionnaireResponseRepository;
	private QuestionnaireRepository questionnaireRepository;

	private TeethRepository teethRepository;
	
	private static String currentPage = LandingPages.CUSTOMER.value();

	
	/**
	 * @param customerRepository
	 */
	@Autowired
	public CustomerModelController(CustomerRepository customerRepository, AccountRepository accountRepository, 
			RoleRepository roleRepository,
			TeethRepository teethRepository,
			RecordRepository recordRepository,
			QuestionnaireRepository questionnaireRepository,
			QuestionnaireResponseRepository questionnaireResponseRepository) {
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;	
		this.teethRepository = teethRepository;
		this.roleRepository = roleRepository;
		this.questionnaireRepository = questionnaireRepository;
		this.questionnaireResponseRepository = questionnaireResponseRepository;
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
				if(account.getCustomer().getCustomerHistory() != null && account.getCustomer().getQuestionnaire() != null) {
					questionnaireRepository.deleteById(account.getCustomer().getQuestionnaire().getId());
				}
				
				account.setCustomer(null);
				account.setRoles(null);
				accountRepository.delete(account);
			});
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {

			Optional<Account> acc = accountRepository.findById(newCustomer.getId());
			acc.ifPresent(account -> {
				logger.debug(" Editing new Customer with username = " + account.getUsername());
				
				if(super.getAllControllerParams().containsKey("profileimage")) {
					MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("profileimage");
					if(uploadedFile != null && (uploadedFile.getOriginalFilename()!= null || uploadedFile.getOriginalFilename() != "")){
						StringBuilder attachmentPath = new StringBuilder();
						try {
							String path = FileManager.write(uploadedFile, "/tmp/customer/profile/");
						    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
						    String fileName = date + uploadedFile.getOriginalFilename();
							attachmentPath.append(fileName);
						} catch (IOException e) {
							e.printStackTrace();
						}
						account.setImage(attachmentPath.toString());
					}else {
						account.setImage(newCustomer.getAccount().getImage());
					}
				}

				Date birthday = newCustomer.getAccount().getBirthday();
				Date today = new Date();
				Period period = DateUtil.getPeriodBetween(birthday, today);
				newCustomer.getAccount().setAge(period.getYears());
				
				newCustomer.setAccount(account);
				newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
				account.setCustomer(newCustomer); 
				accountRepository.saveAndFlush(account);
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			logger.debug(" Creating new Customer with username = " + newCustomer.getAccount().getUsername());

			if(newCustomer.getAccount().getRoles().get(0).getName().equals("CUSTOMER")) {		
				newCustomer.getAccount().setCustomer(null);
				newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
				newCustomer.getAccount().setPersonnel(null);
				newCustomer.getAccount().setActive(true);
				newCustomer.getAccount().setEnabled(true);
				newCustomer.getAccount().setPassword(new BCryptPasswordEncoder().encode(newCustomer.getAccount().getPassword()));
				if(super.getAllControllerParams().get("profileimage") != null) {
					
					MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("profileimage");
					System.out.println(uploadedFile.toString());
					System.out.println(uploadedFile.getOriginalFilename());
					if(!uploadedFile.isEmpty() && (uploadedFile.getOriginalFilename()!= null || !uploadedFile.isEmpty())){
							StringBuilder attachmentPath = new StringBuilder();
							try {
								String path = FileManager.write(uploadedFile, "/tmp/customer/profile/");
							    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
							    String fileName = date + uploadedFile.getOriginalFilename();
								attachmentPath.append(fileName);
							} catch (IOException e) {
								e.printStackTrace();
							} 
							newCustomer.getAccount().setImage(attachmentPath.toString());
					}else {
						newCustomer.getAccount().setImage(newCustomer.getAccount().getImage());
					}
				}
				Date birthday = newCustomer.getAccount().getBirthday();
				Date today = new Date();
				Period period = DateUtil.getPeriodBetween(birthday, today);
				newCustomer.getAccount().setAge(period.getYears());
				
				Account newAccount = accountRepository.saveAndFlush(newCustomer.getAccount());
				newCustomer.setAccount(newAccount);
				customerRepository.saveAndFlush(newCustomer);
			}
			
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
	private void buildCustomerViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			Customer newCustomer = new Customer();
			newCustomer.setAccount(new Account());
			super.addModelCollectionToView("selected", newCustomer);

			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isCustomerCreation", true);

		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String customerId = super.getAllControllerParams().get("id").toString();
			Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));

			if(super.hasResultBindingError()) {
				if(super.getAllControllerParams().containsKey("modelAttribute")) {
					
					Account selectedAcc = (Account) super.getAllControllerParams().get("modelAttribute");

					Optional<Account> oldAccount = accountRepository.findById(selectedAcc.getId());
					oldAccount.ifPresent(oldaccount -> {
						setImageToAccount((MultipartFile)super.getAllControllerParams().get("profileimage"), customer.get().getAccount(), oldaccount);
					});
				}
			}

			customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));
			
			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String customerId = super.getAllControllerParams().get("id").toString();
				Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
				customer.ifPresent(x -> {
					Optional<Questionnaire> questionnaire = questionnaireRepository.findByCustomerId(x.getId());
					questionnaire.ifPresent(singlequestionnaire -> {					
						List<QuestionnaireResponse> questionnaireResponses = questionnaireResponseRepository.findAllByQuestionnaireIdAndResponse(singlequestionnaire.getId(), "yes");
						super.addModelCollectionToView("anamezeAllergies", questionnaireResponses);
					});
					super.addModelCollectionToView("selected", x);
				});
				
				List<Teeth> teeths = teethRepository.findAll();
				super.addModelCollectionToView("listTeeth", teeths);		
				
				
				
				Iterable<Role> allRoles = roleRepository.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void buildCustomerGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		
		
		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});

		Iterable<Customer> customers = customerRepository.findAll();
		super.addModelCollectionToView("customerList", customers);
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
