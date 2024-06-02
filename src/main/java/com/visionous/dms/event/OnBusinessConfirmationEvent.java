package com.visionous.dms.event;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.visionous.dms.configuration.helpers.DmsCore;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnBusinessConfirmationEvent  extends ApplicationEvent{

    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private String appUrl;
    private Locale locale;
    private Account account;
    private Business business;

    public OnBusinessConfirmationEvent(
			Account user, Business business, Locale locale, String appUrl) {
        super(user);
        
        this.account = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.business = business;
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

	/**
	 * @return
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}
	
	
}