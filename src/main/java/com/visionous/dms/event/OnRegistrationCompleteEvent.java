/**
 * 
 */
package com.visionous.dms.event;

import com.o2dent.lib.accounts.entity.Account;
import com.visionous.dms.configuration.helpers.DmsCore;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * @author delimeta
 *
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent{

    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private String appUrl;
    private Locale locale;
    private Account account;

    public OnRegistrationCompleteEvent(
      Account user, Locale locale, String appUrl) {
        super(user);
        
        this.account = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

	/**
	 * @return the appUrl
	 */
	public String getAppUrl() {
		return appUrl;
	}

	/**
	 * @param appUrl the appUrl to set
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the user
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the user to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
}
