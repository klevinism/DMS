/**
 * 
 */
package com.visionous.dms.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
public class OnResetPasswordEvent  extends ApplicationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private String appUrl;
    private Locale locale;
    private Account account;
	
	/**
	 * @param source
	 */
    public OnResetPasswordEvent(Account account, Locale locale, String appUrl) {
    	super(account);    	        
        this.account = account;
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
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
    
}
