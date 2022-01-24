/**
 * 
 */
package com.visionous.dms.rest;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.PhoneNumberExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Business;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.AccountService;
import com.visionous.dms.service.BusinessService;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.RoleService;

/**
 * @author delimeta
 *
 */
@RestController
public class CustomerRestController {
	private MessageSource messageSource;
	
	private CustomerService customerService;
	private AccountService accountService;
	private RoleService roleService;

	private BusinessService businessService;
	
	/**
	 * 
	 */
	@Autowired
	public CustomerRestController(MessageSource messageSource, CustomerService customerService, 
			RoleService roleService, AccountService accountService, BusinessService businessService) {
		 this.messageSource = messageSource;
		 this.customerService = customerService;
		 this.roleService = roleService;
		 this.accountService = accountService;
		 this.businessService = businessService;
	}
	
	@PostMapping("/api/account/create/simple")
    public ResponseEntity<?> createSimpleAccount(@RequestParam(name = "fullname", required = true) String fullName,
    		@RequestParam(name = "birthday", required = false) Date birthday,
    		@RequestParam(name = "gender", required = false) String gender,
    		@RequestParam(name = "phone", required = true) Long phoneNr,
    		@RequestParam(name = "email", required = false) String email) {
		
    	String messagefullNameWrongFormat = messageSource.getMessage("alert.fullNameFormat", null, LocaleContextHolder.getLocale());

		String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
		
		String messageCustomerExists = messageSource.getMessage("alert.customerExists", null, LocaleContextHolder.getLocale());

		String messagePhoneNumberExists = messageSource.getMessage("alert.phoneNumberExists", null, LocaleContextHolder.getLocale());

        ResponseBody<Customer> result = new ResponseBody<>();
        
    	String pattern = "(\\w+)\\s+(\\w+)";
        
        if (fullName.matches(pattern)) {
        	String[] nameArr = fullName.split(" ");

        	nameArr[0] = nameArr[0].trim();
        	nameArr[0] = nameArr[0].toLowerCase();
        	nameArr[1] = nameArr[1].trim();
        	nameArr[1] = nameArr[1].toLowerCase();
    		
    		Optional<Account> potential = this.accountService.findByNameAndSurnameAndBirthday(nameArr[0], nameArr[1], birthday);
    		
    		if(!potential.isPresent()) {
    		
        		String username = nameArr[0]+"."+nameArr[1];
	        	
	        	Optional<Role> customerRole = roleService.findByName("CUSTOMER");
	        	
	        	Account acc = new Account();
	        	acc.setName(StringUtils.capitalize(nameArr[0]));
	        	acc.setSurname(StringUtils.capitalize(nameArr[1]));
	        	if(birthday != null) {
	        		acc.setAge(AccountUtil.calculateAgeFromBirthday(birthday));
	        	}else {
		        	acc.setAge(0);
	        	}
	        	acc.setUsername(username);
	        	acc.setBirthday(birthday);
	        	acc.setEmail(email);
	        	acc.setGender(gender);
        		acc.setPhone(phoneNr);

	        	acc.setPersonnel(null);
	        	
	        	customerRole.ifPresent(role -> acc.addRole(role));
	        	
				
	        	Customer newCustomer = new Customer();
	        	newCustomer.setAccount(acc);
	        	
	        	try {

					

		        	Business loggedInBusiness = AccountUtil.currentLoggedInBussines();
		        	loggedInBusiness.getAccounts().add(newCustomer.getAccount());
		        	newCustomer.getAccount().addBusiness(loggedInBusiness);
					Customer createdCustomer = customerService.create(newCustomer);

					Business updatedBusiness = businessService.update(loggedInBusiness);
					AccountUtil.setCurrentLoggedInBusiness(updatedBusiness);

					result.addResult(createdCustomer);
					
				} catch (EmailExistsException e) {
					result.setError(messageError);
					result.setMessage(messageCustomerExists);

					result.addResult(findAccountByEmailOrPhone(email, phoneNr));
					
				} catch (UsernameExistsException e) {
					result.setError(messageError);
					result.setMessage(messageCustomerExists);

					result.addResult(findAccountByEmailOrPhone(email, phoneNr));
				} catch (PhoneNumberExistsException e) {
					// TODO Auto-generated catch block
					result.setError(messageError);
					result.setMessage(messagePhoneNumberExists);

					result.addResult(findAccountByEmailOrPhone(email, phoneNr));
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}else {
    			result.setError(messageError);
				result.setMessage(messagePhoneNumberExists);

				result.addResult(findAccountByEmailOrPhone(email, phoneNr));
    		}
    	}else {
    		result.setError(messageError);
    		result.setMessage(messagefullNameWrongFormat);
		}
        
        return ResponseEntity.ok(result);
	}

	/**
	 * @param email
	 * @param phoneNr
	 */
	private Customer findAccountByEmailOrPhone(String email, Long phoneNr) {
		if(email != null) {
			Optional<Account> findByEmail = accountService.findByUsernameOrEmail(email);

			if(findByEmail.isPresent()) {
				return findByEmail.get().getCustomer();
			}
		}else if(phoneNr != null) {
			Optional<Account> findByPhone = accountService.findByPhoneNr(phoneNr);

			if(findByPhone.isPresent()) {
				return findByPhone.get().getCustomer();
			}
		}
		return null;
	}
}
