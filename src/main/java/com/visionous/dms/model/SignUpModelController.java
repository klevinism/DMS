package com.visionous.dms.model;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.PhoneNumberExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.IaoAccount;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.service.AccountService;
import com.visionous.dms.service.RoleService;

@Controller
public class SignUpModelController extends ModelControllerImpl {

	private final Log logger = LogFactory.getLog(SignUpModelController.class);

	private static String currentPage = LandingPages.REGISTER.value();
	
	private AccountService accountService;

	private ApplicationEventPublisher eventPublisher;

	
	// Bean
	private ApplicationContext ctx;
	
	private MessageSource messageSource;

	private RoleService roleService;
	
	/**
	 * 
	 */
	@Autowired
	public SignUpModelController( ApplicationContext ctx, AccountService accountService, 
			MessageSource messageSource, ApplicationEventPublisher eventPublisher, RoleService roleService) {
		this.accountService = accountService;
		this.messageSource = messageSource;
		this.eventPublisher = eventPublisher;
		this.roleService = roleService;
		this.ctx = ctx;
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
						(IaoAccount) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);					
				}
			}
		}
		
		// Build view
		this.buildGlobalSettingsViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Customer
		this.buildGlobalSettingsGlobalViewModel();
	}
	/**
	 * 
	 */
	private void persistModelAttributes(IaoAccount iaoaccount, String action) {
		IaoAccount iaoAccount = iaoaccount;
		
		String messageEmailExists = messageSource.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messageSource.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
        String messagePhoneNumberExists = messageSource.getMessage("alert.phoneNumberExists", null, LocaleContextHolder.getLocale());
        String messageInvalidEmailOrPhone = messageSource.getMessage("alert.invalidEmailOrPhone", null, LocaleContextHolder.getLocale());
        String messageInvalidPassword = messageSource.getMessage("alert.invalidPassword", null, LocaleContextHolder.getLocale());
        String messageInvalidName = messageSource.getMessage("alert.invalidName", null, LocaleContextHolder.getLocale());
        String messageInvalidSurname = messageSource.getMessage("alert.invalidSurname", null, LocaleContextHolder.getLocale());
        String messageInvalidConsent = messageSource.getMessage("alert.invalidName", null, LocaleContextHolder.getLocale());
        String messageSelectGender = messageSource.getMessage("alert.selectGender", null, LocaleContextHolder.getLocale());
        String messageSelectBirthday = messageSource.getMessage("alert.selectBirthday", null, LocaleContextHolder.getLocale());

		if(action.equals(Actions.DELETE.getValue())) {
		} else if(action.equals(Actions.EDIT.getValue()) ) {
			
//			if(globalSetting.getId() != null) {
//				if(super.getAllControllerParams().containsKey("businessImage")) {
//					try {
//						String imageName = null;
//						if((imageName = uploadBusinessImage()) != null) {
//							globalSetting.setBusinessImage(imageName);
//						}else {
//							globalSetting.setBusinessImage(AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessImage());
//						}
//					} catch (IOException e) {
//							e.printStackTrace();
//					}						
//				}
//
//				GlobalSettings newSetting = globalSettingsService.update(globalSetting);
//			}
			
		} else if(action.equals(Actions.CREATE.getValue())) {
			Pattern patternText = Pattern.compile("^[a-z]{3,}$");
			Pattern patternPassword = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
			
			//RFC822 compliant email regex
			Pattern patternEmail = Pattern.compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");
			
			Account newAccount = new Account();
			newAccount.setCustomer(null);
			newAccount.setPersonnel(null);
			
			if(patternText.matcher(iaoAccount.getName()).matches()) {
				newAccount.setName(iaoAccount.getName());
			}else {
				super.getBindingResult().addError(
						new FieldError("name", "name", iaoAccount.getName(), false, null, null, messageInvalidName));
			}
			if(patternText.matcher(iaoAccount.getSurname()).matches()) {
				newAccount.setSurname(iaoAccount.getSurname());
			}else {
				super.getBindingResult().addError(
						new FieldError("surname", "surname", iaoAccount.getSurname(), false, null, null, messageInvalidSurname));
			}

			if(iaoAccount.getBirthday() != null) {
				newAccount.setBirthday(iaoAccount.getBirthday());
			}else {
				super.getBindingResult().addError(
						new FieldError("birthday", "birthday", iaoAccount.getBirthday(), false, null, null, messageSelectBirthday));
			}
			
			if(iaoAccount.getGender() != null) {
				newAccount.setGender(iaoAccount.getGender());
			}else {
				super.getBindingResult().addError(
						new FieldError("gender", "gender", null, false, null, null, messageSelectGender));
			}
			
			if(patternPassword.matcher(iaoAccount.getPassword()).matches()) {
				newAccount.setPassword(iaoAccount.getPassword());
			}else {
				super.getBindingResult().addError(
						new FieldError("password", "password", null, false, null, null, messageInvalidPassword));
			}
			
			if(iaoAccount.getTerms()) {
//				newAccount.setPassword(iaoAccount.getPassword()); TODO add terms to account object
			}else {
				super.getBindingResult().addError(
						new FieldError("terms", "terms", null, false, null, null, messageInvalidConsent));
			}
			
			newAccount.setAccount(true);
			
			try {
				PhoneNumberUtil phoneNrUtils = PhoneNumberUtil.getInstance();				
				
				PhoneNumber phoneNumber = phoneNrUtils.parse(iaoAccount.getEmail(), CountryCodeSource.UNSPECIFIED.name());
				
				if(PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
					int countryCode = phoneNumber.getCountryCode();
					Long number = phoneNumber.getNationalNumber();
					
					newAccount.setPhone(number);
				}else if(patternEmail.matcher(iaoAccount.getEmail()).matches()) {
					newAccount.setEmail(iaoAccount.getEmail());
				}else {
					super.getBindingResult().addError(
							new FieldError("email", "email", iaoAccount.getEmail(), false, null, null, messageInvalidEmailOrPhone));
				}
				
			}catch(NumberParseException e) {
				if(patternEmail.matcher(iaoAccount.getEmail()).matches()) {
					newAccount.setEmail(iaoAccount.getEmail());
				}else {
					super.getBindingResult().addError(
							new FieldError("email", "email", iaoAccount.getEmail(), false, null, null, messageInvalidEmailOrPhone));
				}
				
				logger.error("Cannot convert "+ iaoAccount.getEmail() +" to phone nr!");
				logger.error(e);
			}
			
			try {
				if(!super.hasResultBindingError()) {
					Optional<Role> roleAdmin = roleService.findByName("ADMIN");
					roleAdmin.ifPresent(role -> {
						newAccount.addRole(role);
					});
					
					Account createdAccount = accountService.create(newAccount);
					eventPublisher.publishEvent(
			        		new OnRegistrationCompleteEvent(createdAccount, LocaleContextHolder.getLocale(), DmsCore.appMainPath())
			    		);
				}
			}catch(EmailExistsException emailExists) {
				super.getBindingResult().addError(
						new FieldError("email", "email", newAccount.getEmail(), false, null, null, messageEmailExists));
				
				logger.error(messageEmailExists);
				
		        super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
			} catch (UsernameExistsException e) {
				super.getBindingResult().addError(
						new FieldError("register", "username", newAccount.getUsername(), false, null, null, messageUsernameExists));
				
				logger.error(messageUsernameExists);
				
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
			} catch (PhoneNumberExistsException e) {
				super.getBindingResult().addError(
						new FieldError("email", "email", null, false, null, null, messagePhoneNumberExists));

				logger.error(messagePhoneNumberExists);
				
			    super.removeControllerParam("viewType");
				super.addControllerParam("viewType", Actions.CREATE.getValue());
			}
			super.addModelCollectionToView("iaoAccount", iaoAccount);

		} else if(action.equals(Actions.VIEW.getValue())) {
		}		
	}
	
	
	/**
	 * 
	 */
	private void buildGlobalSettingsViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);

		if(viewType.equals(Actions.CREATE.getValue())) {
			if(!super.getAllControllerParams().containsKey("modelAttribute")) {
				super.addModelCollectionToView("iaoAccount", new IaoAccount());
			}
		}else if(viewType.equals(Actions.DELETE.getValue())){
			
		}else if (viewType.equals(Actions.EDIT.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			
		}
		
		if(super.hasResultBindingError()) {
			if(!super.getAllControllerParams().containsKey("modelAttribute")) {
				super.clearResultBindingErrors();
			}
		}
	}
	
	/**
	 * 
	 */
	private void buildGlobalSettingsGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);

		super.addModelCollectionToView("currentPage", currentPage);
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
	}
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> SignUpModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
	
}
