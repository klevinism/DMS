package com.visionous.dms.event;

import java.util.Locale;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.visionous.dms.pojo.GlobalSettings;
import org.springframework.context.ApplicationEvent;

import com.visionous.dms.configuration.helpers.DmsCore;

public class OnSubscriptionConfirmationEvent extends ApplicationEvent{

    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private String appUrl;
    private Locale locale;
    private Account account;
	private Business business;
	private GlobalSettings globalSettings;

    public OnSubscriptionConfirmationEvent(Account user, Business business, GlobalSettings globalSettings,
										   Locale locale, String appUrl) {
        super(user);
        
        this.account = user;
        this.business = business;
		this.globalSettings = globalSettings;
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
	public GlobalSettings getGlobalSettings() {return globalSettings;}
	public void setGlobalSettings(GlobalSettings globalSettings) {this.globalSettings = globalSettings;}
}