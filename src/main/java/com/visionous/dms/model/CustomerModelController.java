/**
 * 
 */
package com.visionous.dms.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.PhoneNumberExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.service.BusinessService;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.QuestionnaireResponseService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.RoleService;
import com.visionous.dms.service.TeethService;

/**
 * @author delimeta
 *
 */
@Controller
public class CustomerModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private RoleService roleService;
    private MessageSource messages;
	private TeethService teethService;
	
	private QuestionnaireResponseService questionnaireResponseService;
	private RecordService recordService;
	private CustomerService customerService;
	private BusinessService businessService;
	
	private static String currentPage = LandingPages.CUSTOMER.value();

	@Autowired
	public CustomerModelController(RoleService roleService, TeethService teethService,
			MessageSource messages,
			RecordService recordService, CustomerService customerService,
			BusinessService businessService,
			QuestionnaireResponseService questionnaireResponseService) {
		
		this.teethService = teethService;
		this.roleService = roleService;
		this.messages=messages;
		this.customerService = customerService;
		this.recordService = recordService;
		this.businessService = businessService;
		this.questionnaireResponseService = questionnaireResponseService;
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
						(Customer) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
				}
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

		String messageEmailExists = messages.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messages.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
        String messagePhoneNumberExists = messages.getMessage("alert.phoneNumberExists", null, LocaleContextHolder.getLocale());

		if(action.equals(Actions.DELETE.getValue())) {
			customerService.deleteByIdAndAccount_Businesses_Id(newCustomer.getId(),AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Customer> selectedCustomer = customerService.findByIdAndAccount_Businesses_Id(newCustomer.getId(), AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());

			selectedCustomer.ifPresent(oldCustomer -> {		
				logger.debug(" Editing new Customer with username = " + oldCustomer.getAccount().getUsername());
				
				try {
					String imageName = null;
					if((imageName = uploadProfileImage()) != null) {
						newCustomer.getAccount().setImage(imageName);
					}
					
					customerService.update(oldCustomer, newCustomer);
					
				} catch (IOException e) {
					logger.error(e.getMessage());
				} catch (EmailExistsException e) {				
					super.getBindingResult().addError(new FieldError("account", "account.email", newCustomer.getAccount().getEmail(), false, null, null, messageEmailExists));
					logger.error(messageEmailExists);
					
			        super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				} catch (UsernameExistsException e) {
					super.getBindingResult().addError(new FieldError("account", "account.username", newCustomer.getAccount().getUsername(), false, null, null, messageUsernameExists));
					logger.error(messageUsernameExists);
					
				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				} catch (PhoneNumberExistsException e) {

					super.getBindingResult().addError(
							new FieldError("account", "account.phone", newCustomer.getAccount().getPhone(), false, null, null, messagePhoneNumberExists));
					
					logger.error(messagePhoneNumberExists);
					
				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				}
				
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			logger.debug(" Creating new Customer with email = " + newCustomer.getAccount().getEmail());

			if(newCustomer.getAccount().getCustomer() != null) {
				
				
				try {
					String imageName = null;
					if((imageName = uploadProfileImage()) != null) {
						newCustomer.getAccount().setImage(imageName);
					}

					Customer createdCustomer = customerService.create(newCustomer);
					
					Business loggedInBusiness = AccountUtil.currentLoggedInBussines();
					loggedInBusiness.getAccounts().add(createdCustomer.getAccount());
					createdCustomer.getAccount().addBusiness(loggedInBusiness);
					
					Business updatedBusiness = businessService.update(loggedInBusiness);
					AccountUtil.setCurrentLoggedInBusiness(updatedBusiness);
					
				} catch (IOException e) {
					logger.error(e.getMessage());
				} catch (EmailExistsException | UsernameExistsException e) {
					super.getBindingResult().addError(
							new FieldError("account", "account.email", newCustomer.getAccount().getEmail(), false, null, null, messageEmailExists));
					logger.error(messageEmailExists);
					
			        super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				} catch (PhoneNumberExistsException e) {

					super.getBindingResult().addError(
							new FieldError("account", "account.phone", newCustomer.getAccount().getPhone(), false, null, null, messagePhoneNumberExists));

					logger.error(messagePhoneNumberExists);
					
				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
				}
			}
		}else if(action.equals(Actions.VIEW.getValue())) {}	
	}
	
	private String uploadProfileImage() throws IOException {
		return FileManager.uploadImage((MultipartFile) super.getAllControllerParams().get("profileimage"), FileManager.CUSTOMER_PROFILE_IMAGE_PATH);
	}

	/**
	 * @param viewType 
	 */
	private void buildCustomerViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			if(!super.hasResultBindingError()) {
				Customer newCustomer = new Customer();
				Account newAccount = new Account();
				newAccount.setPersonnel(null);
				newCustomer.setAccount(newAccount);
				super.addModelCollectionToView("customer", newCustomer);
			}
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isCustomerCreation", true);
		}else if(viewType.equals(Actions.DELETE.getValue())) {
			String customerId = super.getAllControllerParams().get("id").toString();
			Optional<Customer> customer = customerService.findByIdAndAccount_Businesses_Id(Long.valueOf(customerId),AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
			customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				Optional<Customer> customer = customerService.findByIdAndAccount_Businesses_Id(customerId, AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
				
				customer.ifPresent(singleCustomer -> {
					super.addModelCollectionToView("selected", singleCustomer);
					
					if(singleCustomer.hasQuestionnaire()) { 
						super.addModelCollectionToView("anamezeAllergies", 
								questionnaireResponseService.findAllByQuestionIdAndResponse(singleCustomer.getQuestionnaire().getId(), "yes")
							);
					}

					if(singleCustomer.hasCustomerHistory()) {
						super.addModelCollectionToView("customerRecords", 
								recordService.findTop5ByHistoryIdOrderByServicedateDesc(singleCustomer.getCustomerHistory().getId())
							);
					}
					
				});

				List<Teeth> teeths = teethService.findAll();
				super.addModelCollectionToView("listTeeth", teeths);		
				
				Iterable<Role> allRoles = roleService.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}
			
		}
		if(super.hasResultBindingError()) {
			if(!super.getAllControllerParams().containsKey("modelAttribute")) {
				super.clearResultBindingErrors();
			}
		}
		super.addModelCollectionToView("lastCustomerRecord", 
				recordService.findLastRecordForAllCustomers(customerService.findAllByAccount_Businesses_Id(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId()))
				);
	}
	
	/**
	 * 
	 */
	private void buildCustomerGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);

		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("customerList", customerService.findAllByAccount_Businesses_Id(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId()));

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		if(AccountUtil.currentLoggedInBussines().getGlobalSettings() != null) {
			System.out.println(AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessStartTimes());
			super.addModelCollectionToView("disabledDays", AccountUtil.currentLoggedInBussines().getGlobalSettings().getNonBusinessDays());
			super.addModelCollectionToView("bookingSplit", AccountUtil.currentLoggedInBussines().getGlobalSettings().getAppointmentTimeSplit());
			super.addModelCollectionToView("startTime", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessStartTimes()[0]);
			super.addModelCollectionToView("startMinute", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessStartTimes()[1]);
			super.addModelCollectionToView("endTime", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessEndTimes()[0]);
			super.addModelCollectionToView("endMinute", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessEndTimes()[1]);
			super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessImage());
		}
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBussines().getActiveSubscription());

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
