/**
 * 
 */
package com.visionous.dms.model;

import java.io.IOException;
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
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Teeth;
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
	private GlobalSettings globalSettings;
	
	private QuestionnaireResponseService questionnaireResponseService;
	private RecordService recordService;
	private CustomerService customerService;
	private static String currentPage = LandingPages.CUSTOMER.value();

	@Autowired
	public CustomerModelController(RoleService roleService, TeethService teethService,
			MessageSource messages, GlobalSettings globalSettings,
			RecordService recordService, CustomerService customerService,
			QuestionnaireResponseService questionnaireResponseService) {
		
		this.teethService = teethService;
		this.roleService = roleService;
		this.messages=messages;
		this.globalSettings = globalSettings;
		this.customerService = customerService;
		this.recordService = recordService;
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
		
		if(action.equals(Actions.DELETE.getValue())) {
			customerService.deleteById(newCustomer.getId());
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Customer> selectedCustomer = customerService.findById(newCustomer.getId());

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
				}
				
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			logger.debug(" Creating new Customer with email = " + newCustomer.getAccount().getEmail());

			if(newCustomer.getAccount().getRoles().get(0).getName().equals("CUSTOMER")) {

				if(newCustomer.getAccount().getUsername() == null) {
					String name = newCustomer.getAccount().getName();
					String surname = newCustomer.getAccount().getSurname();
					newCustomer.getAccount().setUsername(name+"."+surname);
					newCustomer.getAccount().setPassword(name+"."+surname+".1234");
				}
				
				
					try {
						String imageName = null;
						if((imageName = uploadProfileImage()) != null) {
							newCustomer.getAccount().setImage(imageName);
						}
						customerService.create(newCustomer);
						
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (EmailExistsException | UsernameExistsException e) {
						super.getBindingResult().addError(new FieldError("account", "account.email", newCustomer.getAccount().getEmail(), false, null, null, messageEmailExists));
						logger.error(messageEmailExists);
						
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
				newCustomer.setAccount(new Account());
				super.addModelCollectionToView("customer", newCustomer);
			}
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isCustomerCreation", true);
		}else if(viewType.equals(Actions.DELETE.getValue())) {
			String customerId = super.getAllControllerParams().get("id").toString();
			Optional<Customer> customer = customerService.findById(Long.valueOf(customerId));
			customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				Optional<Customer> customer = customerService.findById(customerId);
				
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
		super.addModelCollectionToView("lastCustomerRecord", 
				recordService.findLastRecordForAllCustomers(customerService.findAll())
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
		
		super.addModelCollectionToView("customerList", customerService.findAll());

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		if(this.globalSettings != null) {
			super.addModelCollectionToView("disabledDays", this.globalSettings.getNonBusinessDays());
			super.addModelCollectionToView("bookingSplit", this.globalSettings.getAppointmentTimeSplit());
			super.addModelCollectionToView("startTime", this.globalSettings.getBusinessStartTimes()[0]);
			super.addModelCollectionToView("startMinute", this.globalSettings.getBusinessStartTimes()[1]);
			super.addModelCollectionToView("endTime", this.globalSettings.getBusinessEndTimes()[0]);
			super.addModelCollectionToView("endMinute", this.globalSettings.getBusinessEndTimes()[1]);
			super.addModelCollectionToView("logo", this.globalSettings.getBusinessImage());
		}
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
