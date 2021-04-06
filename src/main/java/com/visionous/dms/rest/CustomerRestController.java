/**
 * 
 */
package com.visionous.dms.rest;

import java.util.Date;
import java.util.Optional;

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
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.AccountService;
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
	
	/**
	 * 
	 */
	@Autowired
	public CustomerRestController(MessageSource messageSource, CustomerService customerService, RoleService roleService, AccountService accountService) {
		 this.messageSource = messageSource;
		 this.customerService = customerService;
		 this.roleService = roleService;
		 this.accountService = accountService;
	}
	
	@PostMapping("/api/account/create/simple")
    public ResponseEntity<?> createSimpleAccount(@RequestParam(name = "fullname", required = true) String fullName,
    		@RequestParam(name = "birthday", required = true) Date birthday,
    		@RequestParam(name = "gender", required = true) String gender,
    		@RequestParam(name = "phone", required = false) Long phoneNr,
    		@RequestParam(name = "email", required = false) String email) {
		
		String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
		
		String messageCustomerExists = messageSource.getMessage("alert.customerExists", null, LocaleContextHolder.getLocale());

        ResponseBody<Customer> result = new ResponseBody<>();
        if(fullName != null && fullName.length() > 1) {
        	String[] nameArr = fullName.split(" ");

        	if(nameArr.length < 2) {
            	String messagefullNameWrongFormat = messageSource.getMessage("alert.fullNameFormat", null, LocaleContextHolder.getLocale());
        		result.setError(messageError);
        		result.setMessage(messagefullNameWrongFormat);
        	}else {
            	nameArr[0] = nameArr[0].trim();
            	nameArr[0] = nameArr[0].toLowerCase();
            	nameArr[1] = nameArr[1].trim();
            	nameArr[1] = nameArr[1].toLowerCase();
        		
        		Optional<Account> potential = this.accountService.findByNameAndSurnameAndBirthday(nameArr[0], nameArr[1], birthday);
        		
        		if(!potential.isPresent()) {
        		
	        		String username = nameArr[0]+"."+nameArr[1];
		        	String pass = nameArr[0]+"."+nameArr[1]+".1234";
		        	
		        	Optional<Role> customerRole = roleService.findByName("CUSTOMER");
		        	
		        	Account acc = new Account();
		        	acc.setName(StringUtils.capitalize(nameArr[0]));
		        	acc.setSurname(StringUtils.capitalize(nameArr[1]));
		        	acc.setAge(AccountUtil.calculateAgeFromBirthday(birthday));
		        	acc.setUsername(username);
		        	acc.setPassword(new BCryptPasswordEncoder().encode(pass));
		        	acc.setBirthday(birthday);
		        	acc.setEmail(email);
		        	acc.setGender(gender);
		
		        	if(email != null) {
		        		acc.setEmail(email.trim());
		        	}else {
		        		acc.setEmail(nameArr[0]+"."+nameArr[1]+"@hotmail.com");
		        	}
		        	
		        	if(phoneNr != null) {
		        		acc.setPhone(phoneNr);
		        	}else {
		        		acc.setPhone(Long.parseLong("0694444333"));
		        	}
		        	
		        	customerRole.ifPresent(role -> acc.addRole(role));
		        	
		        	Customer newCustomer = new Customer();
		        	newCustomer.setAccount(acc);
		        	
		        	try {
						result.addResult(customerService.create(newCustomer));
						
					} catch (EmailExistsException e) {
						result.setError(messageError);
						result.setMessage(messageCustomerExists);

						result.addResult(findAccountByEmailOrPhone(email.trim(), phoneNr));
						
					} catch (UsernameExistsException e) {
						result.setError(messageError);
						result.setMessage(messageCustomerExists);

						result.addResult(findAccountByEmailOrPhone(email.trim(), phoneNr));
					}
        		}else {
        			result.setError(messageError);
					result.setMessage(messageCustomerExists);

					result.addResult(findAccountByEmailOrPhone(email.trim(), phoneNr));
        		}
        	}
        }else {	
        	String messagefullNameWrongFormat = messageSource.getMessage("alert.fullNameFormat", null, LocaleContextHolder.getLocale());
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
