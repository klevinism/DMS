package com.visionous.dms.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.lib.accounts.entity.Role;
import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.o2dent.lib.accounts.persistence.BusinessService;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.IaoAccount_Customer;
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
	private QuestionnaireResponseService questionnaireResponseService;
	private RecordService recordService;
	private CustomerService customerService;
	private AccountService accountService;
	private BusinessService businessService;
	
	private static String currentPage = LandingPages.CUSTOMER.value();

	@Autowired
	public CustomerModelController(RoleService roleService, TeethService teethService,
			MessageSource messages,
			RecordService recordService, CustomerService customerService,
			BusinessService businessService, AccountService accountService,
			QuestionnaireResponseService questionnaireResponseService) {
		
		this.teethService = teethService;
		this.roleService = roleService;
		this.messages=messages;
		this.customerService = customerService;
		this.recordService = recordService;
		this.businessService = businessService;
		this.accountService = accountService;
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
						(IaoAccount_Customer) super.getAllControllerParams().get("modelAttribute"),
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
	private void persistModelAttributes(IaoAccount_Customer iao, String action) {

		String messageEmailExists = messages.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messages.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
        String messagePhoneNumberExists = messages.getMessage("alert.phoneNumberExists", null, LocaleContextHolder.getLocale());

		if(action.equals(Actions.DELETE.getValue())) {
//			customerService.deleteByIdAndAccount_Businesses_Id(newCustomer.getId(),AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Customer> selectedCustomer = customerService.findById(iao.getAccount().getId());

				selectedCustomer.ifPresent(oldCustomer -> {
					logger.debug(" Editing new Customer with username = " + iao.getAccount().getUsername());

					try {
						String imageName = null;
						if((imageName = uploadProfileImage()) != null) {
							iao.getAccount().setImage(imageName);
						}
						accountService.update(iao.getAccount());
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (EmailExistsException e) {
						super.getBindingResult().addError(new FieldError("account", "account.email", iao.getAccount().getEmail(), false, null, null, messageEmailExists));
						logger.error(messageEmailExists);

						super.removeControllerParam("viewType");
						super.addControllerParam("viewType", Actions.CREATE.getValue());
					} catch (UsernameExistsException e) {
						super.getBindingResult().addError(new FieldError("account", "account.username", iao.getAccount().getUsername(), false, null, null, messageUsernameExists));
						logger.error(messageUsernameExists);

						super.removeControllerParam("viewType");
						super.addControllerParam("viewType", Actions.CREATE.getValue());
					}

				});
		}else if(action.equals(Actions.CREATE.getValue())) {
				logger.debug(" Creating new Customer with email = " + iao.getAccount().getEmail());

			try {
				String imageName = null;
				if((imageName = uploadProfileImage()) != null) {
					iao.getAccount().setImage(imageName);
				}

				Business loggedInBusiness = AccountUtil.currentLoggedInUser().getCurrentBusiness();

				iao.getAccount().addBusiness(loggedInBusiness);
				Account newAccount = accountService.create(iao.getAccount());

				Business updatedBusiness = businessService.update(loggedInBusiness);
				AccountUtil.setCurrentLoggedInBusiness(updatedBusiness);

				Customer newCustomer = new Customer();
				newCustomer.setId(newAccount.getId());
				Customer createdCustomer = customerService.create(newCustomer);

			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (EmailExistsException | UsernameExistsException e) {
				super.getBindingResult().addError(
						new FieldError("account", "account.email", iao.getAccount().getEmail(), false, null, null, messageEmailExists));
				logger.error(messageEmailExists);

				super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
			} catch (PhoneNumberExistsException e) {

				super.getBindingResult().addError(
						new FieldError("account", "account.phone", iao.getAccount().getPhone(), false, null, null, messagePhoneNumberExists));

				logger.error(messagePhoneNumberExists);

				super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
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
				super.addModelCollectionToView("iaoAccount_Customer",
						new IaoAccount_Customer(new Account(),  new Customer()));
				super.addModelCollectionToView("account", new Account());
			}
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isCustomerCreation", true);
		}else if(viewType.equals(Actions.DELETE.getValue())) {
			String customerId = super.getAllControllerParams().get("id").toString();
			Optional<Account> account = accountService.findByIdAndBusinesses_Id(Long.valueOf(customerId),AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
			account.ifPresent(a -> {
				Optional<Customer> customer = customerService.findById(a.getId());
				customer.ifPresent(c -> super.addModelCollectionToView("iaoAccount_Customer", new IaoAccount_Customer(a, c)));
			});

			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());

				Optional<Account> customerAccount = accountService.findByIdAndBusinesses_Id(Long.valueOf(customerId),AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
				customerAccount.ifPresent(a -> {
					Optional<Customer> customer = customerService.findById(a.getId());
					customer.ifPresent(singleCustomer -> {
						super.addModelCollectionToView("iaoAccount_Customer", new IaoAccount_Customer(a, singleCustomer));
						super.addModelCollectionToView("account", a);

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
		List<Account> selectedAccounts = accountService.findAllByBusinessIdAndRoles_Name(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), "CUSTOMER");
		List<Customer> customerList = customerService.findAllByIdIn(selectedAccounts.stream().map(a -> a.getId()).collect(Collectors.toList()));
		super.addModelCollectionToView("lastCustomerRecord",recordService.findLastRecordForAllCustomers(customerList));
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

		List<Account> accountList = accountService.findAllByBusinessIdAndRoles_Name(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), "CUSTOMER");
		List<Customer> customerList = customerService.findAllByIdIn(accountList.stream().map(a -> a.getId()).collect(Collectors.toList()));
		super.addModelCollectionToView("accountList", accountList);
		super.addModelCollectionToView("customerList", customerList);

		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		if(AccountUtil.currentLoggedInBusinessSettings() != null) {
			super.addModelCollectionToView("disabledDays", AccountUtil.currentLoggedInBusinessSettings().getNonBusinessDays());
			super.addModelCollectionToView("bookingSplit", AccountUtil.currentLoggedInBusinessSettings().getAppointmentTimeSplit());
			super.addModelCollectionToView("startTime", AccountUtil.currentLoggedInBusinessSettings().getBusinessStartTimes()[0]);
			super.addModelCollectionToView("startMinute", AccountUtil.currentLoggedInBusinessSettings().getBusinessStartTimes()[1]);
			super.addModelCollectionToView("endTime", AccountUtil.currentLoggedInBusinessSettings().getBusinessEndTimes()[0]);
			super.addModelCollectionToView("endMinute", AccountUtil.currentLoggedInBusinessSettings().getBusinessEndTimes()[1]);
			super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		}
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

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
