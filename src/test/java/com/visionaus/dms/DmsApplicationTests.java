package com.visionaus.dms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.visionous.dms.repository.AccountRepository;

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
	}

}
