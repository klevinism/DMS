/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.time.Period;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.pojo.Business;

/**
 * @author delimeta
 *
 */
public class AccountUtil {
	private final static Log logger = LogFactory.getLog(AccountUtil.class);

	
	private AccountUtil() {
	}
	
	public static AccountUserDetail currentLoggedInUser(){
		AccountUserDetail loggedIn = null;
		
		try {
			loggedIn = (AccountUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		}catch(Exception e) {
			
			e.printStackTrace();

			logger.error(e.getMessage());
		}
		
		return loggedIn;
	}
	
	public static Business currentLoggedInBussines(){
		return currentLoggedInUser().getCurrentBusiness();
	}
	
	public static int calculateAgeFromBirthday(Date birthday) {
		Date today = new Date();
		Period period = DateUtil.getPeriodBetween(birthday, today);
		return period.getYears();
	}
	
	public static String getCurrentLocaleLanguageAndCountry() {
		return LocaleContextHolder.getLocale().getLanguage() + "_" + LocaleContextHolder.getLocale().getCountry();
	}
	
	public static void setCurrentLoggedInBusiness(Business updatedBusiness) {
		currentLoggedInUser().setCurrentBusiness(updatedBusiness);
	}
}
