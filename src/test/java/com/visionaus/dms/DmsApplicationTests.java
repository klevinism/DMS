package com.visionaus.dms;

import com.o2dent.lib.accounts.helpers.validators.UrlValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;

class DmsApplicationTests {
//	@Autowired
//	private AccountRepository accountRepository;
//	
//	@Test
//	void contextLoads() {
//		System.out.println(accountRepository.findByUsernameOrEmail("klevindelimeta@hotmail.com", "klevindelimeta@hotmail.com").get());
//	}
	
	@Test
	public void givenPhoneNumber_whenValid_thenOK() throws NumberParseException {
		PhoneNumberUtil phoneUtils = PhoneNumberUtil.getInstance();
	    PhoneNumber phone = phoneUtils.parse("+355696433538", 
	      CountryCodeSource.UNSPECIFIED.name());

	    System.out.println(phoneUtils.isValidNumber(phone));
	    System.out.println(phoneUtils.isValidNumberForRegion(phone, "IN"));
	    System.out.println(phoneUtils.isValidNumberForRegion(phone, "US"));
	    System.out.println(phoneUtils.isValidNumber(phoneUtils.getExampleNumber("IN")));
//	    
//        Pattern txt_pattern = Pattern.compile("^[a-zA-Z0-9]+$");
//        String txt = "expressionexpression";
//        
//        if(Objects.nonNull(txt) && txt_pattern.matcher(txt).find()) {
//        	Assert.isTrue(true);
//        }else {
//        	Assert.isTrue(false);
//        }
//	    
	}
	
	@Test
	public void testEmailValidation() {
		ConstraintValidatorContext cvc = Mockito.mock(ConstraintValidatorContext.class);
		Assert.isTrue(new UrlValidator().isValid("google.com", cvc), "true");
	}

}
