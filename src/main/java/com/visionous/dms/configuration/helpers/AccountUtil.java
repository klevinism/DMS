/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.time.Period;
import java.util.Date;

import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.security.bundle.authentication.access.O2AccountInfo;
import com.visionous.dms.pojo.GlobalSettings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author delimeta
 *
 */
public class AccountUtil {
	private static O2AccountInfo loggedIn = null;
	private final static Log logger = LogFactory.getLog(AccountUtil.class);

	public static O2AccountInfo currentLoggedInUser(){
		var ctx = SecurityContextHolder.getContext();

		if(ctx.getAuthentication() == null ) return null;

		if(ctx.getAuthentication().isAuthenticated() && !ctx.getAuthentication().getPrincipal().equals("anonymousUser")){
			try {
				loggedIn = (O2AccountInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				loggedIn.setAccount(loggedIn.getAccount());
			}catch(Exception e) {
				// e.printStackTrace();
				logger.error(e.getMessage());
			}
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
	public static GlobalSettings currentLoggedInBusinessSettings() {
		return (GlobalSettings)AccountUtil.currentLoggedInUser().getCurrentBusinessSettings();
	}
}
